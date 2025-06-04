package com.monoexpenses.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.domain.repository.CategoryRepository
import com.monoexpenses.domain.usecase.CategorizeTransactionsUseCase
import com.monoexpenses.domain.usecase.GetTransactionsUseCase
import com.monoexpenses.domain.usecase.MoveTransactionToCategoryUseCase
import com.monoexpenses.utils.getDisplayName
import com.monoexpenses.utils.toEpochSeconds
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

private const val TAG = "HomeViewModel"

data class HomeState(
    val categorizationData: CategorizationData? = null,
    val errorMessage: String? = null,
    val loading: Boolean? = null,
    val showAddBankAccount: Boolean = false,
    val showCalendarDialog: Boolean = false,
    val selectedDateMessage: String = "",
    val categories: List<Category> = emptyList(),
)

class HomeViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val categorizeTransactionsUseCase: CategorizeTransactionsUseCase,
    private val categoryRepository: CategoryRepository,
    private val moveTransactionToCategoryUseCase: MoveTransactionToCategoryUseCase,
) : ViewModel() {

    private val coroutineContext = IODispatcher.io + CoroutineExceptionHandler { _, throwable ->
        Logger.e(TAG, throwable) { "CoroutineExceptionHandler" }
        emitNewState {
            copy(
                loading = false,
                errorMessage = throwable.toString(),
                showCalendarDialog = false,
            )
        }
    }
    private val _stateFlow = MutableStateFlow(HomeState())
    val stateFlow = _stateFlow.asStateFlow()

    fun onLoadClick() {
        emitNewState { copy(showCalendarDialog = true) }
    }

    fun loadData(startDate: LocalDate, endDate: LocalDate) {
        Logger.d(TAG) { "loadData" }
        emitNewState {
            copy(
                loading = true,
                errorMessage = null,
                showCalendarDialog = false,
            )
        }

        viewModelScope.launch(coroutineContext) {
            val categories = categoryRepository.getCategories()
            val transactions = getTransactionsUseCase.execute(
                fromMillis = startDate.toEpochSeconds(localTime = LocalTime(0, 0)),
                toMillis = endDate.toEpochSeconds(localTime = LocalTime(23, 59, 59)),
            )
            val categorizationData = categorizeTransactionsUseCase.execute(transactions, categories)
            emitNewState {
                copy(
                    loading = false,
                    categorizationData = categorizationData,
                    showCalendarDialog = false,
                    selectedDateMessage = formatSelectedDateMessage(startDate, endDate),
                    categories = categories,
                )
            }
        }
    }

    private fun formatSelectedDateMessage(startDate: LocalDate, endDate: LocalDate) =
        "${startDate.month.getDisplayName()} " +
                "${startDate.dayOfMonth} - ${endDate.month.getDisplayName()} " +
                "${endDate.dayOfMonth}"

    fun displayAddBankAccount() {
        emitNewState { copy(showAddBankAccount = true) }
    }

    fun dismissAddBankAccount() {
        emitNewState { copy(showAddBankAccount = false) }
    }

    fun dismissCalendar() {
        emitNewState { copy(showCalendarDialog = false) }
    }

    fun resetError() {
        emitNewState { copy(errorMessage = null) }
    }

    fun moveTransactionToCategory(transaction: Transaction, category: Category) {
        val categorizationData = stateFlow.value.categorizationData
        if (categorizationData != null) {
            val modifiedCategorizationData = moveTransactionToCategoryUseCase.execute(
                categorizationData, transaction, category,
            )
            emitNewState { copy(categorizationData = modifiedCategorizationData) }
        }
    }

    private inline fun emitNewState(block: HomeState.() -> HomeState) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }
}

