package com.monoexpenses.common

import kotlinx.coroutines.CoroutineDispatcher

actual object IODispatcher {
    actual val io: CoroutineDispatcher
        get() = TODO("Not yet implemented")
}