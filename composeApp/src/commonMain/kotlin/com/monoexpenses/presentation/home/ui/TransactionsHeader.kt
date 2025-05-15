package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monoexpenses.presentation.ui.theme.AppColors

@Composable
fun TransactionsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.ListHeaderColor)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Date", modifier = Modifier.weight(1f))
        Text("Sum", modifier = Modifier.weight(1f))
        Text("Name", modifier = Modifier.weight(2f))
    }
}
