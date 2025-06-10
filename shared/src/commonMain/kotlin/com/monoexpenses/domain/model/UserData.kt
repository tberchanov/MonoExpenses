package com.monoexpenses.domain.model

data class UserData(
    val id: String,
    val token: String,
    val name: String? = null,
)
