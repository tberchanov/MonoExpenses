package com.monoexpenses.presentation.categories.configuration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CategoriesConfigurationState(
    val categories: Any? = null,
)

class CategoriesConfigurationViewModel : ViewModel() {

    private val _stateFlow = MutableStateFlow(CategoriesConfigurationState())
    val stateFlow = _stateFlow.asStateFlow()
}
