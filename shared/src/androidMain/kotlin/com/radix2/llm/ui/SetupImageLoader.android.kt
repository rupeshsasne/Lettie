package com.radix2.llm.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import okhttp3.OkHttpClient

private const val USER_AGENT =
    "LastLetterMaster/1.0 (https://github.com/rupeshsasne/Lettie; educational kids app)"

@Composable
actual fun SetupImageLoader() {
    val context = LocalContext.current.applicationContext
    setSingletonImageLoaderFactory {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", USER_AGENT)
                    .build()
                chain.proceed(request)
            }
            .build()
        ImageLoader.Builder(context)
            .components {
                add(OkHttpNetworkFetcherFactory(callFactory = { client }))
            }
            .build()
    }
}
