package com.monoexpenses.data.category

import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter

internal interface CategoriesLocalDataSource {
    suspend fun getCategories(): List<Category>
    suspend fun saveCategory(category: Category)
    suspend fun deleteCategory(categoryId: String)
    suspend fun saveCategoryFilter(categoryId: String, filter: CategoryFilter)
    suspend fun deleteCategoryFilter(categoryId: String, filter: CategoryFilter)
}
