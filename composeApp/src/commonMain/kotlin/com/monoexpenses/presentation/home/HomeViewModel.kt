package com.monoexpenses.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.domain.repository.CategoryRepository
import com.monoexpenses.domain.usecase.CategorizeTransactionsUseCase
import com.monoexpenses.domain.usecase.GetTransactionsUseCase
import com.monoexpenses.domain.usecase.MoveTransactionsToCategoryUseCase
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
    val categorizationData: CategorizationData = CategorizationData(emptyList(), emptyList(), 0),
    val errorMessage: String? = null,
    val loading: Boolean? = null,
    val showCalendarDialog: Boolean = false,
    val selectedDateMessage: String = "",
    val categories: List<Category> = emptyList(),
    val selectedTransactions: Set<TransactionFullData> = emptySet(),
    val allLoadedTransactions: List<TransactionFullData> = emptyList(),
)

class HomeViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val categorizeTransactionsUseCase: CategorizeTransactionsUseCase,
    private val categoryRepository: CategoryRepository,
    private val moveTransactionsToCategoryUseCase: MoveTransactionsToCategoryUseCase,
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

    private val movedTransactionsToCategory = mutableMapOf<TransactionFullData, Category>()

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
                    allLoadedTransactions = transactions,
                )
            }
            movedTransactionsToCategory.clear()
        }
    }

    private fun formatSelectedDateMessage(startDate: LocalDate, endDate: LocalDate) =
        "${startDate.month.getDisplayName()} " +
                "${startDate.dayOfMonth} - ${endDate.month.getDisplayName()} " +
                "${endDate.dayOfMonth}"

    fun dismissCalendar() {
        emitNewState { copy(showCalendarDialog = false) }
    }

    fun resetError() {
        emitNewState { copy(errorMessage = null) }
    }

    fun moveTransactionToCategory(transaction: TransactionFullData, category: Category) {
        val categorizationData = stateFlow.value.categorizationData
        val transactionsToMove = if (stateFlow.value.selectedTransactions.isEmpty()) {
            listOf(transaction)
        } else {
            stateFlow.value.selectedTransactions.toList()
        }
        val modifiedCategorizationData = moveTransactionsToCategoryUseCase.execute(
            categorizationData, transactionsToMove, category,
        )
        emitNewState {
            copy(
                categorizationData = modifiedCategorizationData,
                selectedTransactions = emptySet(),
            )
        }
        transactionsToMove.forEach {
            movedTransactionsToCategory[it] = category
        }
    }

    fun selectTransaction(transaction: TransactionFullData, isSelected: Boolean) {
        emitNewState {
            val newSelectedTransactions = if (isSelected) {
                selectedTransactions.toMutableSet() + transaction
            } else {
                selectedTransactions.toMutableSet() - transaction
            }
            copy(selectedTransactions = newSelectedTransactions)
        }
    }

    fun closeSelection() {
        emitNewState {
            copy(selectedTransactions = emptySet())
        }
    }

    private inline fun emitNewState(block: HomeState.() -> HomeState) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }

    fun recategorizeTransactions() {
        Logger.d(TAG) { "recategorizeTransactions" }

        viewModelScope.launch(coroutineContext) {
            val categories = categoryRepository.getCategories()

            val categorizationData = categorizeTransactionsUseCase.execute(
                stateFlow.value.allLoadedTransactions, categories,
            ).let(::applyMovedTransactions)

            emitNewState {
                copy(
                    categorizationData = categorizationData,
                    categories = categories,
                )
            }
        }
    }

    private fun applyMovedTransactions(
        categorizationData: CategorizationData,
    ): CategorizationData {
        var modifiedCategorizationData = categorizationData
        movedTransactionsToCategory.forEach { (transactionFullData, category) ->
            modifiedCategorizationData = moveTransactionsToCategoryUseCase.execute(
                modifiedCategorizationData,
                listOf(transactionFullData),
                category,
            )
        }
        return modifiedCategorizationData
    }
}

