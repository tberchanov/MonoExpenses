package com.monoexpenses.common

import kotlinx.coroutines.CoroutineDispatcher

expect object IODispatcher {
    val io: CoroutineDispatcher
}
