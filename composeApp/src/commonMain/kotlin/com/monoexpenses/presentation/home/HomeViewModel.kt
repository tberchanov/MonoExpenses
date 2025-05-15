package com.monoexpenses.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.usecase.CategorizeTransactionsUseCase
import com.monoexpenses.domain.usecase.GetTransactionsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TAG = "AppViewModel"

data class HomeState(
    val categorizationData: CategorizationData? = null,
    val errorMessage: String? = null,
    val loading: Boolean? = null,
    val showAddBankAccount: Boolean = false,
)

class HomeViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val categorizeTransactionsUseCase: CategorizeTransactionsUseCase,
) : ViewModel() {

    private val coroutineContext = IODispatcher.io + CoroutineExceptionHandler { _, throwable ->
        Logger.e(TAG, throwable) { "CoroutineExceptionHandler" }
        emitNewState { copy(loading = false, errorMessage = throwable.toString()) }
    }
    private val _stateFlow = MutableStateFlow(HomeState())
    val stateFlow = _stateFlow.asStateFlow()

    @OptIn(ExperimentalTime::class)
    fun loadData() {
        Logger.d(TAG) { "loadData" }
        emitNewState { copy(loading = true, errorMessage = null) }

        viewModelScope.launch(coroutineContext) {
            val transactions = getTransactionsUseCase.execute(
                fromMillis = 1746046800000,
                toMillis = Clock.System.now().toEpochMilliseconds()
            )
            val categorizationData = categorizeTransactionsUseCase.execute(transactions)
            emitNewState { copy(loading = false, categorizationData = categorizationData) }
        }
    }

    fun displayAddBankAccount() {
        emitNewState { copy(showAddBankAccount = true) }
    }

    fun dismissAddBankAccount() {
        emitNewState { copy(showAddBankAccount = false) }
    }

    fun resetError() {
        emitNewState { copy(errorMessage = null) }
    }

    private inline fun emitNewState(block: HomeState.() -> HomeState) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }
}

