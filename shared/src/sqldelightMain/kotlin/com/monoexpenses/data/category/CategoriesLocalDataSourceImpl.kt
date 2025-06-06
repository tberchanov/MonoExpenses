package com.monoexpenses.data.category

import co.touchlab.kermit.Logger
import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter

private const val TAG = "CategoryRepositoryLocalImpl"

internal class CategoriesLocalDataSourceImpl(
    private val database: DBProvider
) : CategoriesLocalDataSource {
    override suspend fun getCategories(): List<Category> {
        Logger.d(TAG) { "getCategories" }

        return database.queries().getAllCategories().executeAsList().map { category ->
            val filters =
                database.queries().getCategoryFilters(category.id).executeAsList().map { filter ->
                    CategoryFilter(
                        transactionMcc = filter.transactionMcc?.toInt(),
                        transactionDescription = filter.transactionDescription,
                        transactionAmount = filter.transactionAmount
                    )
                }

            Category(
                id = category.id,
                name = category.name,
                categoryFilters = filters
            )
        }
    }

    override suspend fun saveCategory(category: Category) {
        Logger.d(TAG) { "saveCategory: $category" }

        database.queries().transaction {
            database.queries().insertCategory(category.id, category.name)

            // Delete existing filters for this category
            database.queries().getCategoryFilters(category.id).executeAsList().forEach { filter ->
                database.queries().deleteCategoryFilter(filter.id)
            }

            // Insert new filters
            category.categoryFilters.forEach { filter ->
                database.queries().insertCategoryFilter(
                    categoryId = category.id,
                    transactionMcc = filter.transactionMcc?.toLong(),
                    transactionDescription = filter.transactionDescription,
                    transactionAmount = filter.transactionAmount
                )
            }
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        Logger.d(TAG) { "deleteCategory: $categoryId" }
        database.queries().deleteCategory(categoryId)
    }

    override suspend fun saveCategoryFilter(categoryId: String, filter: CategoryFilter) {
        Logger.d(TAG) { "saveCategoryFilter: categoryId=$categoryId, filter=$filter" }

        database.queries().insertCategoryFilter(
            categoryId = categoryId,
            transactionMcc = filter.transactionMcc?.toLong(),
            transactionDescription = filter.transactionDescription,
            transactionAmount = filter.transactionAmount
        )
    }

    override suspend fun deleteCategoryFilter(categoryId: String, filter: CategoryFilter) {
        Logger.d(TAG) { "deleteCategoryFilter: categoryId=$categoryId, filter=$filter" }
        
        database.queries().transaction {
            // Find the filter by matching all its properties
            val filters = database.queries().getCategoryFilters(categoryId).executeAsList()
            val filterToDelete = filters.find { dbFilter ->
                dbFilter.transactionMcc?.toInt() == filter.transactionMcc &&
                dbFilter.transactionDescription == filter.transactionDescription &&
                dbFilter.transactionAmount == filter.transactionAmount
            }
            
            filterToDelete?.let {
                database.queries().deleteCategoryFilter(it.id)
            }
        }
    }
}