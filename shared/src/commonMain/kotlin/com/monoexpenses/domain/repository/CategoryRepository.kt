package com.monoexpenses.domain.repository

import com.monoexpenses.domain.model.Category

interface CategoryRepository {

    fun getCategories(): List<Category>
}