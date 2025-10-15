package com.monoexpenses.presentation.accounts.configuration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.model.UserBankAccounts
import com.monoexpenses.presentation.accounts.configuration.AccountsConfigurationViewModel
import com.monoexpenses.presentation.add.accounts.ui.AddAccountsDialog
import com.monoexpenses.presentation.ui.ErrorDialog
import expensesanalyzer.composeapp.generated.resources.Res
import expensesanalyzer.composeapp.generated.resources.delete
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountsConfigurationScreen(
    onBackClicked: () -> Unit = {},
) {
    val viewModel = koinViewModel<AccountsConfigurationViewModel>()
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val showAddDialog = remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(
            14.dp,
            onBackClicked = onBackClicked,
        )

        Spacer(Modifier.size(16.dp))
        Button(onClick = { showAddDialog.value = true }, modifier = Modifier.padding(16.dp)) {
            Text("Add User")
        }

        state.apply {
            if (errorMessage != null) {
                ErrorDialog(
                    errorMessage,
                    onDismiss = {
                        viewModel.resetError()
                    }
                )
            }
            data?.let { users ->
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(
                        items = users,
                        key = { "accounts-config-user-" + it.userData.id }
                    ) { userBankAccounts ->
                        UserAccountsItem(
                            userBankAccounts = userBankAccounts,
                            onDelete = { viewModel.deleteUser(userBankAccounts.userData.id) },
                            onAccountToggled = { account, enabled ->
                                val current = userBankAccounts.bankAccounts.toMutableList()
                                if (enabled) {
                                    if (current.none { it.id == account.id }) current.add(account)
                                } else {
                                    current.removeAll { it.id == account.id }
                                }
                                viewModel.updateUserAccounts(userBankAccounts.userData.id, current)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog.value) {
        AddAccountsDialog(
            onDismiss = {
                showAddDialog.value = false
                viewModel.refresh()
            }
        )
    }
}

@Composable
fun UserAccountsItem(
    userBankAccounts: UserBankAccounts,
    onDelete: () -> Unit,
    onAccountToggled: (BankAccount, Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                userBankAccounts.userData.name ?: userBankAccounts.userData.id,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete) {
                Icon(painterResource(Res.drawable.delete), contentDescription = "Delete user")
            }
        }
        Spacer(Modifier.size(8.dp))
        Text("Bank Accounts:", fontSize = 14.sp)
        Column(modifier = Modifier.padding(start = 16.dp)) {
            userBankAccounts.bankAccounts.forEach { account ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { checked -> onAccountToggled(account, checked) }
                    )
                    Text(account.name + " (" + account.currency + ")", fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun AccountsConfigurationScreenPreview() {
    AccountsConfigurationScreen()
}