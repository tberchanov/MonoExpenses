package com.monoexpenses.exception

class UnexpectedResponseException(
    statusCode: Int,
    body: String,
) : IllegalStateException("Status $statusCode, Body: $body")