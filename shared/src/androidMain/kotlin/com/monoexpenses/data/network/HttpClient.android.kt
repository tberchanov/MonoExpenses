package com.monoexpenses.data.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun getHttpEngine(): HttpClientEngineFactory<*> = CIO
