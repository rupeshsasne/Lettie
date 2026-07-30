package com.radix2.llm.sound

import androidx.compose.runtime.Composable

/** Short game sound effects. Kept tiny/synth-based so we ship no audio assets. */
interface SoundPlayer {
    fun correct()
    fun wrong()
    fun win()
    fun tap()
    fun dispose()
}

@Composable
expect fun rememberSoundPlayer(): SoundPlayer

/** Silent player for previews/tests and non-Android targets. */
class NoopSoundPlayer : SoundPlayer {
    override fun correct() {}
    override fun wrong() {}
    override fun win() {}
    override fun tap() {}
    override fun dispose() {}
}
