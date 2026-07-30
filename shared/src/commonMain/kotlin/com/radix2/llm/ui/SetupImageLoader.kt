package com.radix2.llm.ui

import androidx.compose.runtime.Composable

/** Installs the Coil singleton ImageLoader (network + Wikimedia-friendly User-Agent). */
@Composable
expect fun SetupImageLoader()
