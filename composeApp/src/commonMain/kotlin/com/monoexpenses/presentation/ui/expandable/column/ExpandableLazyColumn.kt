package com.monoexpenses.presentation.ui.expandable.column

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> ExpandableLazyColumn(
    expandableItems: List<ExpandableItem<T>>,
    modifier: Modifier = Modifier,
    keyPrefix: String = "",
    collapsedItemContent: @Composable (ExpandableItem<T>) -> Unit,
    expandedItemContent: @Composable (ExpandableItem<T>) -> Unit,
    onItemExpanded: (String, Boolean) -> Unit = { _, _ -> }
) {
    var state by mutableStateOf(expandableItems)

    // Keep internal state in sync when caller provides a new list instance
    LaunchedEffect(expandableItems) {
        state = expandableItems
    }

    LazyColumn(
        modifier,
    ) {
        itemsIndexed(
            state,
            { _, item -> keyPrefix + item.id }
        ) { index, item ->
            Column {
                Box(Modifier.clickable {
                    val newItem = item.copy(isExpanded = !item.isExpanded)
                    state = state.toMutableList()
                        .apply {
                            set(index, newItem)
                        }
                    onItemExpanded(item.id, newItem.isExpanded)
                }) {
                    collapsedItemContent(item)
                }
                AnimatedVisibility(
                    visible = item.isExpanded,
                    enter = expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(
                        initialAlpha = 0.3f
                    ),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top,
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeOut(
                        targetAlpha = 0.3f
                    )
                ) {
                    expandedItemContent(item)
                }
            }
        }
    }
}
