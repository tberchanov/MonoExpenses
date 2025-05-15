package com.monoexpenses

import co.touchlab.kermit.Logger

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        val greeting = "Hello, ${platform.name}!"
        Logger.i { "Greeting: $greeting" }
        return greeting
    }
}