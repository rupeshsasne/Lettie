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
import com.radix2.llm.voice.VoiceController

@Composable
fun VoiceSettingsScreen(
    voice: VoiceController,
    onBack: () -> Unit,
) {
    AppScaffold(title = "Lettie's voice", onBack = onBack) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "System voices · Soft · Indian English",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Pick a voice installed on this phone. Soft style is always on.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))

            FilledTonalButton(
                onClick = {
                    voice.speak("Hi, I'm Lettie! Let's play word chains.")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) {
                Text("Preview Lettie", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            if (voice.availableVoices.isEmpty()) {
                Text(
                    text = "Loading voices… If none appear, install an Indian English voice in your phone's Text-to-speech settings.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(voice.availableVoices, key = { it.id }) { option ->
                        FilterChip(
                            selected = option.id == voice.selectedVoiceId,
                            onClick = {
                                voice.selectVoice(option.id)
                                voice.speak("Hi! This is my ${option.localeTag} voice.")
                            },
                            label = {
                                Text(option.displayName)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
