package com.monoexpenses.presentation.home.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CategoryItemPreview() {
    CategoryItem(
        categoryName = "Category Category",
    )
}

@Preview
@Composable
fun CategoryItemPreviewWithExpenses() {
    CategoryItem(
        categoryName = "Category Category",
        totalExpenses = 42424269,
    )
}
