package com.radix2.llm.ui

import androidx.compose.runtime.Composable

/** Handles the platform back gesture/button so it navigates our stack instead of exiting. */
@Composable
expect fun SystemBackHandler(enabled: Boolean, onBack: () -> Unit)
