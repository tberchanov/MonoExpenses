package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun <T> VerticalList(
    modifier: Modifier,
    items: List<T>,
    key: (T) -> Any,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
        ) {
            items(
                items = items,
                key = key,
            ) { item ->
                itemContent(item)
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(lazyListState)
        )
    }
}
