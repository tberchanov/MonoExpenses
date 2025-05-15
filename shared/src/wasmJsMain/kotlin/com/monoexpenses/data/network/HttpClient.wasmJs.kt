package com.monoexpenses.data.network

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.JsClient

actual fun getHttpEngine(): HttpClientEngineFactory<*> = JsClient()
