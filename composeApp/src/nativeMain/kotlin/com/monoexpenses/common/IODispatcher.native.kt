package com.monoexpenses.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual object IODispatcher {
    actual val io: CoroutineDispatcher
        get() = Dispatchers.IO
}