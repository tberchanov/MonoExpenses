package com.monoexpenses.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.monoexpenses.presentation.home.ui.calendar.ContinuousSelectionHelper.isInDateBetweenSelection
import com.monoexpenses.presentation.home.ui.calendar.ContinuousSelectionHelper.isOutDateBetweenSelection
import com.monoexpenses.presentation.home.ui.calendar.DateSelection
import com.monoexpenses.presentation.ui.shape.HalfSizeShape
import com.monoexpenses.presentation.ui.theme.AppColors
import kotlinx.datetime.LocalDate

fun Modifier.clickable(
    enabled: Boolean = true,
    showRipple: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = if (showRipple) ripple(
            radius = 30.dp,
        ) else null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick,
    )
}

fun Modifier.backgroundHighlight(
    day: CalendarDay,
    today: LocalDate,
    selection: DateSelection,
    selectionColor: Color,
    continuousSelectionColor: Color,
    textColor: (Color) -> Unit,
): Modifier = composed {
    val (startDate, endDate) = selection
    val padding = 4.dp
    when (day.position) {
        DayPosition.MonthDate -> {
            when {
                day.date > today -> {
                    textColor(AppColors.example4GrayPast)
                    this
                }

                startDate == day.date && endDate == null -> {
                    textColor(Color.White)
                    padding(padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                day.date == startDate -> {
                    textColor(Color.White)
                    padding(vertical = padding)
                        .background(
                            color = continuousSelectionColor,
                            shape = HalfSizeShape(clipStart = true),
                        )
                        .padding(horizontal = padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                startDate != null && endDate != null && (day.date > startDate && day.date < endDate) -> {
                    textColor(AppColors.example4Gray)
                    padding(vertical = padding)
                        .background(color = continuousSelectionColor)
                }

                day.date == endDate -> {
                    textColor(Color.White)
                    padding(vertical = padding)
                        .background(
                            color = continuousSelectionColor,
                            shape = HalfSizeShape(clipStart = false),
                        )
                        .padding(horizontal = padding)
                        .background(color = selectionColor, shape = CircleShape)
                }

                day.date == today -> {
                    textColor(AppColors.example4Gray)
                    padding(padding)
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = AppColors.example4GrayPast,
                        )
                }

                else -> {
                    textColor(AppColors.example4Gray)
                    this
                }
            }
        }

        DayPosition.InDate -> {
            textColor(Color.Transparent)
            if (startDate != null &&
                endDate != null &&
                isInDateBetweenSelection(day.date, startDate, endDate)
            ) {
                padding(vertical = padding)
                    .background(color = continuousSelectionColor)
            } else {
                this
            }
        }

        DayPosition.OutDate -> {
            textColor(Color.Transparent)
            if (startDate != null &&
                endDate != null &&
                isOutDateBetweenSelection(day.date, startDate, endDate)
            ) {
                padding(vertical = padding)
                    .background(color = continuousSelectionColor)
            } else {
                this
            }
        }
    }
}
