package com.monoexpenses.presentation.home.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.monoexpenses.presentation.ui.theme.AppColors
import com.monoexpenses.utils.formatMoney
import expensesanalyzer.composeapp.generated.resources.Res
import expensesanalyzer.composeapp.generated.resources.folder_icon
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryItem(
    categoryName: String,
    totalExpenses: Long? = null,
    isExpanded: Boolean = false
) {
    val rotationDegree by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "iconRotationAnimation",
    )

    Column {
        Divider()

        Row(Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(Res.drawable.folder_icon),
                null,
                tint = AppColors.Primary,
                modifier = Modifier.padding(end = 8.dp)
                    .graphicsLayer {
                        rotationZ = rotationDegree
                    }
            )
            Text(
                categoryName,
                modifier = Modifier.padding(end = 8.dp),
            )
            if (totalExpenses != null) {
                Text(
                    remember(totalExpenses) {
                        formatMoney(
                            totalExpenses
                        )
                    },
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}