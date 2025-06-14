package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import expensesanalyzer.composeapp.generated.resources.add_card_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun Header(
    padding: Dp,
    onLoadClick: () -> Unit,
    onAddBankAccountsClick: () -> Unit,
    selectedDateMessage: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.Primary)
            .shadow(4.dp)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selectedDateMessage.isNotEmpty()) {
            Text(
                selectedDateMessage,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                    )
                    .padding(4.dp),
                color = Color.White,
            )
        } else {
            Text(
                "Expenses Analyzer",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            )
        }
        Spacer(Modifier.weight(1f))
        IconButton(onClick = {
            onAddBankAccountsClick()
        }, modifier = Modifier.padding(end = 8.dp)) {
            Icon(painterResource(Res.drawable.add_card_icon), null, tint = Color.White)
        }
        Button(
            onClick = onLoadClick, colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.AccentColor,
            )
        ) {
            Text("Load", color = Color.White)
        }
    }
}
