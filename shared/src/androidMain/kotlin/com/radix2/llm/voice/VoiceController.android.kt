package com.radix2.llm.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
actual fun rememberVoiceController(): VoiceController {
    val context = LocalContext.current
    val controller = remember { AndroidVoiceController(context.applicationContext) }
    DisposableEffect(Unit) {
        onDispose { controller.dispose() }
    }
    return controller
}

/**
 * Android voice implementation.
 * - Speech: [TextToSpeech], slightly slower + higher pitch for a friendly, kid-clear voice.
 * - Listening: [SpeechRecognizer] with a free-form model, returning multiple candidates.
 *
 * Note: SpeechRecognizer must be created/used on the main thread; Compose event
 * callbacks run there, so we do not switch threads.
 */
class AndroidVoiceController(private val appContext: Context) : VoiceController {

    override var isSpeaking by mutableStateOf(false)
        private set
    override var isListening by mutableStateOf(false)
        private set

    private var ttsReady = false
    private var pendingSpeak: Pair<String, () -> Unit>? = null

    private val tts: TextToSpeech = TextToSpeech(appContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
            // Prefer Indian English for this audience; fall back gracefully.
            val india = Locale.forLanguageTag("en-IN")
            val result = tts.setLanguage(india)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val us = tts.setLanguage(Locale.US)
                if (us == TextToSpeech.LANG_MISSING_DATA || us == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.language = Locale.ENGLISH
                }
            }
            tts.setSpeechRate(0.88f)
            tts.setPitch(1.08f)
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

    override fun speak(text: String, onDone: () -> Unit) {
        if (!ttsReady) {
            pendingSpeak = text to onDone
            return
        }
        // Flushing cancels the previous utterance — complete its callback so awaiters don't hang.
        val orphaned = onDoneCallbacks.values.toList()
        onDoneCallbacks.clear()
        orphaned.forEach { runCatching { it.invoke() } }

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
            // Prefer Indian English recognition for local accents / vocabulary.
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
