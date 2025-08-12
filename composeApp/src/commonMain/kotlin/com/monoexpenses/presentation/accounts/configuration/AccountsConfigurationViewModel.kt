package com.monoexpenses.presentation.accounts.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.monoexpenses.common.IODispatcher
import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.model.UserBankAccounts
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.UserDataRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AccountsConfigurationViewModel"

data class AccountsConfigurationState(
    val data: List<UserBankAccounts>? = null,
    val errorMessage: String? = null,
)

class AccountsConfigurationViewModel(
    private val userDataRepository: UserDataRepository,
    private val bankAccountsRepository: BankAccountsRepository,
) : ViewModel() {

    private val coroutineContext = IODispatcher.io + CoroutineExceptionHandler { _, throwable ->
        Logger.e(TAG, throwable) { "CoroutineExceptionHandler" }
        emitNewState {
            copy(
                errorMessage = throwable.toString(),
            )
        }
    }

    private val _stateFlow = MutableStateFlow(AccountsConfigurationState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(coroutineContext) {
            try {
                val users = userDataRepository.getAllUserData()
                val userBankAccounts = users.map { user ->
                    val accounts = bankAccountsRepository.loadSelectedAccounts(user.id)
                    UserBankAccounts(user, accounts)
                }
                emitNewState { copy(data = userBankAccounts) }
            } catch (e: Exception) {
                emitNewState { copy(errorMessage = e.message) }
            }
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch(coroutineContext) {
            try {
                userDataRepository.deleteUserData(userId)
                refresh()
            } catch (e: Exception) {
                emitNewState { copy(errorMessage = e.message) }
            }
        }
    }

    fun updateUserAccounts(userId: String, accounts: List<BankAccount>) {
        viewModelScope.launch(coroutineContext) {
            try {
                bankAccountsRepository.saveSelectedAccounts(userId, accounts)
                refresh()
            } catch (e: Exception) {
                emitNewState { copy(errorMessage = e.message) }
            }
        }
    }

    fun resetError() {
        emitNewState {
            copy(
                errorMessage = null,
            )
        }
    }

    private inline fun emitNewState(
        block: AccountsConfigurationState.() -> AccountsConfigurationState,
    ) {
        val newState = _stateFlow.value.block()
        Logger.d(TAG) { "emitNewState: $newState" }
        _stateFlow.value = newState
    }
}