package com.monoexpenses.presentation.categories.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.repository.CategoryRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "CategoriesConfigurationViewModel"

data class CategoriesConfigurationState(
    val categories: List<Category>? = null,
    val errorMessage: String? = null,
    val showAddCategoryDialog: Boolean = false,
    val showAddCategoryFilterDialog: Boolean = false,
    val categoryToAddFilter: Category? = null,
)

class CategoriesConfigurationViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val coroutineContext = IODispatcher.io + CoroutineExceptionHandler { _, throwable ->
        Logger.e(TAG, throwable) { "CoroutineExceptionHandler" }
        emitNewState {
            copy(
                errorMessage = throwable.toString(),
            )
        }
    }

    private val _stateFlow = MutableStateFlow(CategoriesConfigurationState())
    val stateFlow = _stateFlow.asStateFlow()

    private inline fun emitNewState(
        block: CategoriesConfigurationState.() -> CategoriesConfigurationState,
    ) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }

    fun loadData() {
        viewModelScope.launch(coroutineContext) {
            loadDataInternal()
        }
    }

    private suspend fun loadDataInternal() {
        emitNewState {
            copy(
                errorMessage = null,
                categories = categoryRepository.getCategories(),
            )
        }
    }

    fun resetError() {
        emitNewState {
            copy(
                errorMessage = null,
            )
        }
    }

    fun onAddCategoryClicked() {
        emitNewState {
            copy(
                showAddCategoryDialog = true,
            )
        }
    }

    fun onAddCategoryFilterClicked(category: Category) {
        emitNewState {
            copy(
                showAddCategoryFilterDialog = true,
                categoryToAddFilter = category,
            )
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(coroutineContext) {
            categoryRepository.deleteCategory(category.id)
            loadDataInternal()
        }
    }

    fun deleteCategoryFilter(category: Category, filter: CategoryFilter) {
        viewModelScope.launch(coroutineContext) {
            categoryRepository.deleteCategoryFilter(category.id, filter)
            loadDataInternal() // Reload categories after deleting filter
        }
    }

    fun addCategory(categoryName: String) {
        viewModelScope.launch(coroutineContext) {
            val trimmedName = categoryName.trim()
            if (trimmedName.isNotEmpty()) {
                val newCategory = Category(
                    id = generateCategoryId(),
                    name = trimmedName,
                    categoryFilters = emptyList()
                )
                categoryRepository.saveCategory(newCategory)
                loadDataInternal() // Reload categories after adding new one
            }
            dismissAddCategoryDialog()
        }
    }

    private fun generateCategoryId(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }

    fun dismissAddCategoryDialog() {
        emitNewState {
            copy(
                showAddCategoryDialog = false,
            )
        }
    }

    fun dismissAddCategoryFilterDialog() {
        Logger.d(TAG) { "dismissAddCategoryFilterDialog" }
        emitNewState {
            copy(
                showAddCategoryFilterDialog = false,
                categoryToAddFilter = null,
            )
        }
    }

    fun addCategoryFilter(categoryFilter: CategoryFilter) {
        viewModelScope.launch(coroutineContext) {
            val currentCategory = stateFlow.value.categoryToAddFilter
            Logger.d(TAG) { "addCategoryFilter: $categoryFilter $currentCategory" }
            if (currentCategory != null) {
                categoryRepository.saveCategoryFilter(currentCategory.id, categoryFilter)
                loadDataInternal()
            }
            dismissAddCategoryFilterDialog()
        }
    }
}
