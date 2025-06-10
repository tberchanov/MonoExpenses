package com.monoexpenses.presentation.accounts.configuration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.monoexpenses.presentation.ui.theme.AppColors
import expensesanalyzer.composeapp.generated.resources.Res
import expensesanalyzer.composeapp.generated.resources.arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun Header(
    padding: Dp,
    onBackClicked: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.Primary)
            .shadow(4.dp)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {
            onBackClicked()
        }, modifier = Modifier.padding(end = 8.dp)) {
            Icon(painterResource(Res.drawable.arrow_back), null, tint = Color.White)
        }

        Text(
            "Accounts Configuration",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
        )
    }
}
