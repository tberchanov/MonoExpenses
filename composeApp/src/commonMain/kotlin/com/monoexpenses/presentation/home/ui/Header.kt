package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import monoexpenses.composeapp.generated.resources.Res
import monoexpenses.composeapp.generated.resources.add_card_icon
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Header(
    padding: Dp,
    onLoadClick: () -> Unit,
    onAddBankAccountsClick: () -> Unit,
    selectedDateMessage: String,
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.Primary)
                .shadow(4.dp)
                .padding(padding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Expenses Analyzer",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1.0f),
            )
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
        if (selectedDateMessage.isNotEmpty()) {
            Text(
                selectedDateMessage,
                modifier = Modifier.align(Alignment.Center)
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                    )
                    .padding(4.dp),
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header(
        padding = 14.dp,
        onLoadClick = {},
        onAddBankAccountsClick = {},
        "May 02 - Jun 24",
    )
}
