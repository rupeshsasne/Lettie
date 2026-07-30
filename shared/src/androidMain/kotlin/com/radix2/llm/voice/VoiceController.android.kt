package com.radix2.llm.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/** Soft Lettie style — gentle pace, slightly warmer pitch. */
private const val SoftSpeechRate = 0.82f
private const val SoftPitch = 1.12f

private val PreferredLocale: Locale = Locale.forLanguageTag("en-IN")

@Composable
actual fun rememberVoiceController(
    preferredVoiceId: String?,
    onVoiceSelected: (String) -> Unit,
): VoiceController {
    val context = LocalContext.current
    val onSelected by rememberUpdatedState(onVoiceSelected)
    val controller = remember {
        AndroidVoiceController(
            appContext = context.applicationContext,
            initialVoiceId = preferredVoiceId,
            onVoiceSelected = { onSelected(it) },
        )
    }
    DisposableEffect(Unit) {
        onDispose { controller.dispose() }
    }
    return controller
}

/**
 * Android voice implementation.
 * - Speech: system [TextToSpeech] voices, preferring en-IN, Soft rate/pitch.
 * - Listening: [SpeechRecognizer] with en-IN, returning multiple candidates.
 */
class AndroidVoiceController(
    private val appContext: Context,
    initialVoiceId: String?,
    private val onVoiceSelected: (String) -> Unit,
) : VoiceController {

    override var isSpeaking by mutableStateOf(false)
        private set
    override var isListening by mutableStateOf(false)
        private set
    override var availableVoices by mutableStateOf<List<TtsVoiceOption>>(emptyList())
        private set
    override var selectedVoiceId by mutableStateOf<String?>(initialVoiceId)
        private set

    private var ttsReady = false
    private var pendingSpeak: Pair<String, () -> Unit>? = null
    private var preferredVoiceId: String? = initialVoiceId

    private val tts: TextToSpeech = TextToSpeech(appContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
            configureLanguage()
            applySoftStyle()
            refreshVoicesAndSelect()
            ttsReady = true
            pendingSpeak?.let { (text, onDone) ->
                pendingSpeak = null
                speak(text, onDone)
            }
        }
    }.also { engine ->
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) { isSpeaking = true }
            override fun onDone(utteranceId: String?) {
                isSpeaking = false
                onDoneCallbacks.remove(utteranceId)?.invoke()
            }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                isSpeaking = false
                onDoneCallbacks.remove(utteranceId)?.invoke()
            }
        })
    }

    private val onDoneCallbacks = mutableMapOf<String, () -> Unit>()
    private var utteranceCounter = 0

    private var recognizer: SpeechRecognizer? = null

    override val recognitionAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(appContext)

    override fun selectVoice(id: String) {
        preferredVoiceId = id
        val voice = tts.voices?.firstOrNull { it.name == id } ?: return
        tts.voice = voice
        selectedVoiceId = id
        applySoftStyle()
        onVoiceSelected(id)
    }

    private fun configureLanguage() {
        val result = tts.setLanguage(PreferredLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            val us = tts.setLanguage(Locale.US)
            if (us == TextToSpeech.LANG_MISSING_DATA || us == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts.language = Locale.ENGLISH
            }
        }
    }

    private fun applySoftStyle() {
        tts.setSpeechRate(SoftSpeechRate)
        tts.setPitch(SoftPitch)
    }

    private fun refreshVoicesAndSelect() {
        val all = tts.voices.orEmpty()
        val enIn = all.filter { it.isEnIn() }
        val english = all.filter { it.locale.language.equals("en", ignoreCase = true) }
        val pool = (enIn.ifEmpty { english }).toList()

        availableVoices = pool
            .sortedWith(voicePreferenceComparator())
            .map { it.toOption() }
            .distinctBy { it.id }

        val preferred = preferredVoiceId?.let { id -> pool.firstOrNull { it.name == id } }
        val chosen = preferred ?: pool.minWithOrNull(voicePreferenceComparator())
        if (chosen != null) {
            tts.voice = chosen
            selectedVoiceId = chosen.name
            if (preferredVoiceId == null) {
                preferredVoiceId = chosen.name
                onVoiceSelected(chosen.name)
            }
        }
        applySoftStyle()
    }

    override fun speak(text: String, onDone: () -> Unit) {
        if (!ttsReady) {
            pendingSpeak = text to onDone
            return
        }
        val orphaned = onDoneCallbacks.values.toList()
        onDoneCallbacks.clear()
        orphaned.forEach { runCatching { it.invoke() } }

        applySoftStyle()
        val id = "utt_${utteranceCounter++}"
        onDoneCallbacks[id] = onDone
        isSpeaking = true
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
    }

    override fun stopSpeaking() {
        tts.stop()
        isSpeaking = false
    }

    override fun startListening(onResult: (List<String>) -> Unit, onError: (String) -> Unit) {
        if (!recognitionAvailable) {
            onError("Speech recognition isn't available on this device.")
            return
        }
        stopSpeaking()
        recognizer?.destroy()
        val sr = SpeechRecognizer.createSpeechRecognizer(appContext)
        recognizer = sr
        sr.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) { isListening = true }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { isListening = false }
            override fun onError(error: Int) {
                isListening = false
                onError(errorMessage(error))
            }
            override fun onResults(results: Bundle?) {
                isListening = false
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).orEmpty()
                if (matches.isEmpty()) onError("I didn't catch that.") else onResult(matches)
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-IN")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 8)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
        isListening = true
        sr.startListening(intent)
    }

    override fun stopListening() {
        recognizer?.stopListening()
        isListening = false
    }

    override fun dispose() {
        onDoneCallbacks.clear()
        recognizer?.destroy()
        recognizer = null
        tts.stop()
        tts.shutdown()
    }

    private fun errorMessage(error: Int): String = when (error) {
        SpeechRecognizer.ERROR_AUDIO -> "There was an audio problem."
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is needed."
        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Check your internet and try again."
        SpeechRecognizer.ERROR_NO_MATCH -> "I didn't catch that. Try again!"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "I didn't hear anything. Tap and speak!"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "One sec, still listening…"
        else -> "Let's try that again!"
    }
}

private fun Voice.isEnIn(): Boolean {
    val tag = locale.toLanguageTag()
    return tag.equals("en-IN", ignoreCase = true) ||
        (locale.language.equals("en", ignoreCase = true) && locale.country.equals("IN", ignoreCase = true))
}

private fun Voice.toOption(): TtsVoiceOption {
    val pretty = name
        .substringAfterLast("/")
        .substringAfterLast("#")
        .replace('-', ' ')
        .replace('_', ' ')
        .replaceFirstChar { it.uppercase() }
    val label = buildString {
        append(pretty.ifBlank { name })
        if (locale.toLanguageTag().isNotBlank()) append(" (${locale.toLanguageTag()})")
    }
    return TtsVoiceOption(
        id = name,
        displayName = label,
        localeTag = locale.toLanguageTag(),
    )
}

/**
 * Prefer: en-IN → higher quality → not explicitly male → installed → stable name.
 * Soft style is applied via pitch/rate; we still bias toward friendlier voice names.
 */
private fun voicePreferenceComparator(): Comparator<Voice> = compareBy(
    { if (it.isEnIn()) 0 else 1 },
    { -it.quality },
    { if (it.nameContains("female") || it.nameContains("woman") || it.nameContains("girl")) 0 else 1 },
    { if (it.nameContains("male") || it.nameContains("man") || it.nameContains("boy")) 2 else 0 },
    { if (it.features.contains(TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED)) 1 else 0 },
    { it.name },
)

private fun Voice.nameContains(token: String): Boolean =
    name.contains(token, ignoreCase = true)
