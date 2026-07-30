package com.radix2.llm.voice

import androidx.compose.runtime.Composable

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

@Composable
expect fun rememberVoiceController(): VoiceController

/** A no-op controller for previews and non-voice contexts. */
class NoopVoiceController : VoiceController {
    override val isSpeaking: Boolean = false
    override val isListening: Boolean = false
    override val recognitionAvailable: Boolean = false
    override fun speak(text: String, onDone: () -> Unit) { onDone() }
    override fun stopSpeaking() {}
    override fun startListening(onResult: (List<String>) -> Unit, onError: (String) -> Unit) {
        onError("Voice not available")
    }
    override fun stopListening() {}
    override fun dispose() {}
}
