package com.monoexpenses.data.browser.storage

external interface BrowserStorage {
    fun setItem(key: String, value: String)
    fun getItem(key: String): String?
    fun removeItem(key: String)
    fun clear()
}

external val localStorage: BrowserStorage
external val sessionStorage: BrowserStorage
