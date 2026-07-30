package com.radix2.llm.sound

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

@Composable
actual fun rememberSoundPlayer(): SoundPlayer {
    val player = remember { AndroidSoundPlayer() }
    DisposableEffect(Unit) {
        onDispose { player.dispose() }
    }
    return player
}

/**
 * Uses the system [ToneGenerator] to synthesize cheerful/soft cues without bundling any
 * audio files. Cheap and offline. Tones are guarded so a busy generator never crashes.
 */
class AndroidSoundPlayer : SoundPlayer {

    private val tone: ToneGenerator? = runCatching {
        ToneGenerator(AudioManager.STREAM_MUSIC, 80)
    }.getOrNull()

    override fun correct() {
        play(ToneGenerator.TONE_PROP_BEEP, 150)
    }

    override fun wrong() {
        play(ToneGenerator.TONE_SUP_ERROR, 250)
    }

    override fun win() {
        // A little three-note flourish.
        play(ToneGenerator.TONE_PROP_BEEP, 120)
        play(ToneGenerator.TONE_PROP_BEEP2, 120)
        play(ToneGenerator.TONE_PROP_ACK, 200)
    }

    override fun tap() {
        play(ToneGenerator.TONE_PROP_BEEP, 60)
    }

    private fun play(type: Int, durationMs: Int) {
        runCatching { tone?.startTone(type, durationMs) }
    }

    override fun dispose() {
        runCatching { tone?.release() }
    }
}
