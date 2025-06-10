package com.monoexpenses.presentation.accounts.configuration

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.UserBankAccounts
import com.monoexpenses.presentation.categories.configuration.CategoriesConfigurationState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val TAG = "AccountsConfigurationViewModel"

data class AccountsConfigurationState(
    val data: List<UserBankAccounts>? = null,
    val errorMessage: String? = null,
)

class AccountsConfigurationViewModel : ViewModel() {

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

    fun resetError() {
        emitNewState {
            copy(
                errorMessage = null,
            )
        }
    }
}