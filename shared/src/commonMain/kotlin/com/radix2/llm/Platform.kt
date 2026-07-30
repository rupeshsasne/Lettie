package com.radix2.llm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform