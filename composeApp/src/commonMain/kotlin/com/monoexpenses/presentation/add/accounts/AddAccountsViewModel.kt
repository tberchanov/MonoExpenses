package com.monoexpenses.presentation.add.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.usecase.GetAllAccountsUseCase
import com.monoexpenses.domain.usecase.SaveSelectedAccountsUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AddAccountsViewModel"

data class SelectableBankAccount(
    val bankAccount: BankAccount,
    val isSelected: Boolean,
)

data class AddAccountsState(
    val token: String = "",
    val loadAccountsButtonEnabled: Boolean = false,
    val saveUserAccountsButtonEnabled: Boolean = false,
    val userName: String = "",
    val selectableBankAccounts: List<SelectableBankAccount>? = null,
    val errorMessage: String? = null,
    val isAccountSaved: Boolean = false,
)

class AddAccountsViewModel(
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val saveSelectedAccountsUseCase: SaveSelectedAccountsUseCase,
) : ViewModel() {

    private val coroutineContext = IODispatcher.io + CoroutineExceptionHandler { _, throwable ->
        Logger.e(TAG, throwable) { "CoroutineExceptionHandler" }
        emitNewState { copy(errorMessage = throwable.toString()) }
    }
    private val _stateFlow = MutableStateFlow(
        AddAccountsState(),
    )
    val stateFlow = _stateFlow.asStateFlow()

    private inline fun emitNewState(block: AddAccountsState.() -> AddAccountsState) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }

    fun loadBankAccounts() {
        viewModelScope.launch(coroutineContext) {
            val userBankAccounts = getAllAccountsUseCase.execute(stateFlow.value.token)
            val bankAccounts =
                userBankAccounts.bankAccounts.map { SelectableBankAccount(it, false) }
            emitNewState {
                copy(
                    selectableBankAccounts = bankAccounts,
                    userName = userBankAccounts.userData.name ?: "",
                )
            }
        }
    }

    fun onTokenEntered(token: String) {
        emitNewState {
            copy(token = token, loadAccountsButtonEnabled = token.isNotEmpty())
        }
    }

    fun onBankAccountSelected(bankAccount: SelectableBankAccount) {
        val modifiedBankAccounts = stateFlow.value
            .selectableBankAccounts
            ?.map {
                if (it.bankAccount == bankAccount.bankAccount) {
                    SelectableBankAccount(it.bankAccount, !it.isSelected)
                } else {
                    it
                }
            }
        emitNewState {
            copy(
                selectableBankAccounts = modifiedBankAccounts,
                saveUserAccountsButtonEnabled =
                    modifiedBankAccounts?.any { it.isSelected } ?: false,
            )
        }
    }

    fun resetState() {
        _stateFlow.value = AddAccountsState()
    }

    fun saveUserBankAccounts() {
        Logger.d(TAG) { "saveUserBankAccounts ${stateFlow.value.selectableBankAccounts?.size}" }
        viewModelScope.launch(coroutineContext) {
            with(stateFlow.value) {
                if (!selectableBankAccounts.isNullOrEmpty()) {
                    saveSelectedAccountsUseCase.execute(
                        token,
                        selectableBankAccounts.filter { it.isSelected }
                            .map { it.bankAccount },
                        userName,
                    )
                }
            }
            emitNewState { copy(isAccountSaved = true) }
        }
    }

    override fun onCleared() {
        Logger.d(TAG) { "onCleared" }
        super.onCleared()
    }
}