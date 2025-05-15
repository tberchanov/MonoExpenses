package com.monoexpenses.presentation.ui.expandable.column

data class ExpandableItem<T>(
    val data: T,
    val id: String,
    val isExpanded: Boolean = false,
)
