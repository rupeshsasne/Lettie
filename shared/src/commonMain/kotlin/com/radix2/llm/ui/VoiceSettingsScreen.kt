package com.radix2.llm.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radix2.llm.ui.adaptive.MaxWidthContainer
import com.radix2.llm.ui.theme.LettieDimens
import com.radix2.llm.voice.VoiceController

@Composable
fun VoiceSettingsScreen(
    voice: VoiceController,
    onBack: () -> Unit,
) {
    AppScaffold(title = "Lettie's voice", onBack = onBack) { innerPadding ->
        MaxWidthContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LettieDimens.screenPadding),
            ) {
                Text(
                    text = "Lettie uses Indian English voices from your phone. Pick the one that sounds best.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (voice.availableVoices.isEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "No Indian English (en-IN) voice found. Install “English (India)” in your phone’s Text-to-speech settings, then reopen this screen.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                Spacer(Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = {
                        voice.speak("Hi, I'm Lettie! Let's play word chains.")
                    },
                    enabled = voice.availableVoices.isNotEmpty() || voice.selectedVoiceId != null,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Text("Preview Lettie", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(voice.availableVoices, key = { it.id }) { option ->
                        FilterChip(
                            selected = option.id == voice.selectedVoiceId,
                            onClick = {
                                voice.selectVoice(option.id)
                                voice.speak("Hi! This is my ${option.displayName} voice.")
                            },
                            label = { Text(option.displayName) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
