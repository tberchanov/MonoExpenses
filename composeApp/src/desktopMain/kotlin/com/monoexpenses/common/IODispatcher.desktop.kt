package com.monoexpenses.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object IODispatcher {
    actual val io: CoroutineDispatcher
        get() = Dispatchers.IO
}