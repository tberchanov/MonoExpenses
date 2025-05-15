package com.monoexpenses

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform