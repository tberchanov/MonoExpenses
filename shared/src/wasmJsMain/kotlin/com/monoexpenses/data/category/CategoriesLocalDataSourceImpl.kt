package com.monoexpenses.data.category

import com.monoexpenses.data.browser.storage.localStorage
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CategoryFilterDto(
    val transactionMcc: Int? = null,
    val transactionDescription: String? = null,
    val transactionAmount: Long? = null,
)

@Serializable
private data class CategoryDto(
    val id: String,
    val name: String,
    val categoryFilters: List<CategoryFilterDto>,
)

class CategoriesLocalDataSourceImpl : CategoriesLocalDataSource {
    private val json = Json { ignoreUnknownKeys = true }
    private val categoriesKey = "categories"

    private fun Category.toDto(): CategoryDto = CategoryDto(
        id = id,
        name = name,
        categoryFilters = categoryFilters.map { filter ->
            CategoryFilterDto(
                transactionMcc = filter.transactionMcc,
                transactionDescription = filter.transactionDescription,
                transactionAmount = filter.transactionAmount
            )
        }
    )

    private fun CategoryDto.toDomain(): Category = Category(
        id = id,
        name = name,
        categoryFilters = categoryFilters.map { filter ->
            CategoryFilter(
                transactionMcc = filter.transactionMcc,
                transactionDescription = filter.transactionDescription,
                transactionAmount = filter.transactionAmount
            )
        }
    )

    override suspend fun getCategories(): List<Category> {
        val categoriesJson = localStorage.getItem(categoriesKey) ?: return emptyList()
        return try {
            json.decodeFromString<List<CategoryDto>>(categoriesJson).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveCategory(category: Category) {
        val currentCategories = getCategories().toMutableList()
        val existingIndex = currentCategories.indexOfFirst { it.id == category.id }

        if (existingIndex >= 0) {
            currentCategories[existingIndex] = category
        } else {
            currentCategories.add(category)
        }

        localStorage.setItem(
            categoriesKey,
            json.encodeToString(currentCategories.map { it.toDto() })
        )
    }

    override suspend fun deleteCategory(categoryId: String) {
        val currentCategories = getCategories()
        val updatedCategories = currentCategories.filter { it.id != categoryId }
        localStorage.setItem(
            categoriesKey,
            json.encodeToString(updatedCategories.map { it.toDto() })
        )
    }

    override suspend fun saveCategoryFilter(categoryId: String, filter: CategoryFilter) {
        val currentCategories = getCategories().toMutableList()
        val categoryIndex = currentCategories.indexOfFirst { it.id == categoryId }

        val category = currentCategories[categoryIndex]
        val updatedFilters = category.categoryFilters.toMutableList()

        // Check if filter already exists by comparing all fields
        val existingFilterIndex = updatedFilters.indexOfFirst { existingFilter ->
            existingFilter.transactionMcc == filter.transactionMcc &&
                    existingFilter.transactionDescription == filter.transactionDescription &&
                    existingFilter.transactionAmount == filter.transactionAmount
        }

        if (existingFilterIndex >= 0) {
            // Update existing filter
            updatedFilters[existingFilterIndex] = filter
        } else {
            // Add new filter
            updatedFilters.add(filter)
        }

        // Create updated category with new filters
        val updatedCategory = category.copy(categoryFilters = updatedFilters)
        currentCategories[categoryIndex] = updatedCategory

        // Save to localStorage
        localStorage.setItem(
            categoriesKey,
            json.encodeToString(currentCategories.map { it.toDto() })
        )
    }

    override suspend fun deleteCategoryFilter(categoryId: String, filter: CategoryFilter) {
        val currentCategories = getCategories().toMutableList()
        val categoryIndex = currentCategories.indexOfFirst { it.id == categoryId }

        if (categoryIndex >= 0) {
            val category = currentCategories[categoryIndex]
            val updatedFilters = category.categoryFilters.filter { existingFilter ->
                existingFilter.transactionMcc != filter.transactionMcc ||
                        existingFilter.transactionDescription != filter.transactionDescription ||
                        existingFilter.transactionAmount != filter.transactionAmount
            }

            currentCategories[categoryIndex] = category.copy(categoryFilters = updatedFilters)
            localStorage.setItem(
                categoriesKey,
                json.encodeToString(currentCategories.map { it.toDto() })
            )
        }
    }
}