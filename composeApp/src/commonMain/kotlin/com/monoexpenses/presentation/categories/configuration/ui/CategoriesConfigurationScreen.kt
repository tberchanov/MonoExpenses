package com.monoexpenses.presentation.categories.configuration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.presentation.categories.configuration.CategoriesConfigurationViewModel
import com.monoexpenses.presentation.home.ui.CategoryItem
import com.monoexpenses.presentation.home.ui.Divider
import com.monoexpenses.presentation.ui.ErrorDialog
import com.monoexpenses.presentation.ui.expandable.column.ExpandableItem
import com.monoexpenses.presentation.ui.expandable.column.ExpandableLazyColumn
import expensesanalyzer.composeapp.generated.resources.Res
import expensesanalyzer.composeapp.generated.resources.add
import expensesanalyzer.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoriesConfigurationScreen(
    onBackClicked: () -> Unit,
    onCategoriesModified: () -> Unit,
) {
    val viewModel = koinViewModel<CategoriesConfigurationViewModel>()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(14.dp, onBackClicked = onBackClicked)

        val state by viewModel.stateFlow.collectAsStateWithLifecycle()
        state.apply {
            if (categoriesModified) {
                remember(categoriesModified) {
                    onCategoriesModified()
                }
            }
            if (errorMessage != null) {
                ErrorDialog(
                    errorMessage,
                    onDismiss = {
                        viewModel.resetError()
                    }
                )
            }
            if (categories != null) {
                CategoriesConfigurationList(
                    categories,
                    onAddCategoryClicked = {
                        viewModel.onAddCategoryClicked()
                    },
                    onDeleteCategoryClicked = { category ->
                        viewModel.deleteCategory(category)
                    },
                    onAddCategoryFilterClicked = { category ->
                        viewModel.onAddCategoryFilterClicked(category)
                    },
                    onDeleteCategoryFilterClicked = { category, filter ->
                        viewModel.deleteCategoryFilter(category, filter)
                    }
                )
            }
            if (showAddCategoryDialog) {
                AddCategoryDialog(
                    onAddCategory = { categoryName ->
                        viewModel.addCategory(categoryName)
                    },
                    onDismiss = {
                        viewModel.dismissAddCategoryDialog()
                    }
                )
            }
            if (showAddCategoryFilterDialog && categoryToAddFilter != null) {
                AddCategoryFilterDialog(
                    categoryToAddFilter,
                    onAddFilter = {
                        viewModel.addCategoryFilter(it)
                    },
                    onDismiss = {
                        viewModel.dismissAddCategoryFilterDialog()
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoriesConfigurationList(
    categories: List<Category>,
    onAddCategoryClicked: () -> Unit,
    onDeleteCategoryClicked: (Category) -> Unit,
    onAddCategoryFilterClicked: (Category) -> Unit,
    onDeleteCategoryFilterClicked: (Category, CategoryFilter) -> Unit,
) {
    var expandedItems by remember { mutableStateOf(setOf<String>()) }

    Column {
        AddCategoryButton(
            onClick = onAddCategoryClicked
        )
        ExpandableLazyColumn(
            keyPrefix = "config-categories-",
            expandableItems = categories.map { category ->
                ExpandableItem(
                    data = category,
                    id = category.id,
                    isExpanded = expandedItems.contains(category.id)
                )
            },
            collapsedItemContent = { item ->
                CategoryItemWithDelete(
                    categoryName = item.data.name,
                    isExpanded = item.isExpanded,
                    onDeleteClick = {
                        onDeleteCategoryClicked(item.data)
                    }
                )
            },
            expandedItemContent = { item ->
                CategoryFiltersList(
                    filters = item.data.categoryFilters,
                    onAddFilterClick = {
                        onAddCategoryFilterClicked(item.data)
                    },
                    onDeleteFilterClick = { filter ->
                        onDeleteCategoryFilterClicked(item.data, filter)
                    }
                )
            },
            onItemExpanded = { itemId, isExpanded ->
                expandedItems = if (isExpanded) {
                    expandedItems + itemId
                } else {
                    expandedItems - itemId
                }
            }
        )
    }
}

@Composable
private fun AddCategoryButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.add),
            contentDescription = "Add category",
            tint = Color.Black
        )
        Text(
            text = "Add new category",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Divider()
}

@Composable
private fun CategoryFiltersList(
    filters: List<CategoryFilter>,
    onAddFilterClick: () -> Unit,
    onDeleteFilterClick: (CategoryFilter) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F4F4))
    ) {
        AddFilterButton(onClick = onAddFilterClick)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            filters.forEachIndexed { index, filter ->
                CategoryFilterItem(
                    filter = filter,
                    showDivider = index > 0,
                    onRemoveClick = {
                        onDeleteFilterClick(filter)
                    }
                )
            }
        }
    }
}

@Composable
private fun AddFilterButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.add),
            contentDescription = "Add filter",
            tint = Color.Black
        )
        Text(
            text = "Add new filter",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Divider()
}

@Composable
private fun CategoryFilterItem(
    filter: CategoryFilter,
    showDivider: Boolean,
    onRemoveClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        if (showDivider) {
            Divider(Modifier.padding(bottom = 8.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                filter.transactionMcc?.let { mcc ->
                    Text(
                        text = "MCC: $mcc",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                filter.transactionDescription?.let { description ->
                    Text(
                        text = "Description: $description",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                filter.transactionAmount?.let { amount ->
                    Text(
                        text = "Amount: $amount",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
            IconButton(
                onClick = onRemoveClick
            ) {
                Icon(
                    painterResource(Res.drawable.delete),
                    contentDescription = "Remove filter",
                    tint = Color(0xFFA00030)
                )
            }
        }
    }
}

@Composable
private fun CategoryItemWithDelete(
    categoryName: String,
    isExpanded: Boolean,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        CategoryItem(
            categoryName = categoryName,
            isExpanded = isExpanded
        )
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painterResource(Res.drawable.delete),
                contentDescription = "Remove category",
                tint = Color(0xFFA00030)
            )
        }
    }
}
