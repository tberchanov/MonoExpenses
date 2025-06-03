package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun <T> VerticalList(
    modifier: Modifier,
    items: List<T>,
    key: (T) -> Any,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = items,
            key = key,
        ) { item ->
            itemContent(item)
        }
    }
}