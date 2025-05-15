package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monoexpenses.presentation.ui.theme.AppColors

val DIVIDER_SIZE = 0.7.dp

@Composable
fun Divider() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(DIVIDER_SIZE)
            .background(AppColors.DividerColor),
    )
}