package com.monoexpenses.utils

import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString

fun ClipboardManager.setText(str: String) {
    setText(AnnotatedString(str))
}
