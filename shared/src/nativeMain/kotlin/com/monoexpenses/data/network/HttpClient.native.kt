package com.monoexpenses.data.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun getHttpEngine(): HttpClientEngineFactory<*> = Darwin
