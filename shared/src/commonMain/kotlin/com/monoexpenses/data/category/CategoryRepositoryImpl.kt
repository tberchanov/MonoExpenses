package com.monoexpenses.data.category

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.repository.CategoryRepository

private const val TAG = "CategoryRepositoryImpl"

internal class CategoryRepositoryImpl(
    private val defaultDataSource: DefaultCategoriesDataSource,
    private val localDataSource: CategoriesLocalDataSource,
): CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        Logger.d(TAG) { "getCategories" }
        
        val categories = localDataSource.getCategories()
        if (categories.isEmpty()) {
            Logger.d(TAG) { "No categories found in local storage, loading defaults" }
            val defaultCategories = defaultDataSource.getDefaultCategories()
            defaultCategories.forEach { category ->
                localDataSource.saveCategory(category)
            }
            return defaultCategories.sortedBy { it.name }
        }
        
        return categories.sortedBy { it.name }
    }

    override suspend fun saveCategory(category: Category) {
        Logger.d(TAG) { "saveCategory: $category" }
        localDataSource.saveCategory(category)
    }

    override suspend fun deleteCategory(categoryId: String) {
        Logger.d(TAG) { "deleteCategory: $categoryId" }
        localDataSource.deleteCategory(categoryId)
    }

    override suspend fun saveCategoryFilter(categoryId: String, filter: CategoryFilter) {
        Logger.d(TAG) { "saveCategoryFilter: categoryId=$categoryId, filter=$filter" }
        localDataSource.saveCategoryFilter(categoryId, filter)
    }

    override suspend fun deleteCategoryFilter(categoryId: String, filter: CategoryFilter) {
        Logger.d(TAG) { "deleteCategoryFilter: categoryId=$categoryId, filter=$filter" }
        localDataSource.deleteCategoryFilter(categoryId, filter)
    }
}