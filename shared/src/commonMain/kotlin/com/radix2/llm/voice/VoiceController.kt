package com.radix2.llm.voice

import androidx.compose.runtime.Composable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** A system TTS voice the child/parent can pick for Lettie. */
data class TtsVoiceOption(
    val id: String,
    val displayName: String,
    val localeTag: String,
)

/**
 * Voice abstraction so game/UI code stays platform-agnostic. Android provides the
 * concrete implementation (TextToSpeech + SpeechRecognizer). [isSpeaking]/[isListening]
 * are Compose snapshot state, so composables reading them recompose automatically.
 */
interface VoiceController {
    val isSpeaking: Boolean
    val isListening: Boolean

    /** Whether speech recognition is usable on this device right now. */
    val recognitionAvailable: Boolean

    /** Installed en-IN (or English) system voices available for Lettie. */
    val availableVoices: List<TtsVoiceOption>

    /** Currently selected system voice id, or null while TTS is still loading. */
    val selectedVoiceId: String?

    fun selectVoice(id: String)

    fun speak(text: String, onDone: () -> Unit = {})
    fun stopSpeaking()

    /**
     * Start listening for a single spoken phrase.
     * @param onResult best-first list of transcription candidates.
     * @param onError human-friendly reason (also covers "no match" / permission issues).
     */
    fun startListening(onResult: (List<String>) -> Unit, onError: (String) -> Unit)
    fun stopListening()

    fun dispose()
}

/** Speak and suspend until TTS finishes (or is cancelled). */
suspend fun VoiceController.speakAwait(text: String) {
    suspendCancellableCoroutine { cont ->
        speak(text) {
            if (cont.isActive) cont.resume(Unit)
        }
        cont.invokeOnCancellation { stopSpeaking() }
    }
}

/** Wait until the engine is no longer speaking (e.g. after a fire-and-forget speak). */
suspend fun VoiceController.awaitSilent(pollMs: Long = 50L) {
    while (isSpeaking) {
        kotlinx.coroutines.delay(pollMs)
    }
}

@Composable
expect fun rememberVoiceController(
    preferredVoiceId: String? = null,
    onVoiceSelected: (String) -> Unit = {},
): VoiceController

/** A no-op controller for previews and non-voice contexts. */
class NoopVoiceController : VoiceController {
    override val isSpeaking: Boolean = false
    override val isListening: Boolean = false
    override val recognitionAvailable: Boolean = false
    override val availableVoices: List<TtsVoiceOption> = emptyList()
    override val selectedVoiceId: String? = null
    override fun selectVoice(id: String) {}
    override fun speak(text: String, onDone: () -> Unit) { onDone() }
    override fun stopSpeaking() {}
    override fun startListening(onResult: (List<String>) -> Unit, onError: (String) -> Unit) {
        onError("Voice not available")
    }
    override fun stopListening() {}
    override fun dispose() {}
}
