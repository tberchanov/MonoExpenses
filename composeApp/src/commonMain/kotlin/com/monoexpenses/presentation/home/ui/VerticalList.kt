package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun <T> VerticalList(
    modifier: Modifier = Modifier,
    items: List<T>,
    key: (T) -> Any,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
)

