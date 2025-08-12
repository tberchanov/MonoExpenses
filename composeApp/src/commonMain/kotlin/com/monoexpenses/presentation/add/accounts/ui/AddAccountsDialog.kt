package com.monoexpenses.presentation.add.accounts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.presentation.add.accounts.AddAccountsViewModel
import com.monoexpenses.presentation.ui.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddAccountsDialog(
    onDismiss: () -> Unit,
) {
    val viewModel = koinViewModel<AddAccountsViewModel>()
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    if (state.isAccountSaved) {
        onDismiss()
        viewModel.resetState()
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
            viewModel.resetState()
        },
    ) {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(400.dp)
            ) {
                Text("Add Token", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.token,
                    onValueChange = { viewModel.onTokenEntered(it) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.loadBankAccounts() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.loadAccountsButtonEnabled,
                ) {
                    Text("Load Bank Accounts")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TODO refactor
                state.selectableBankAccounts?.let { accounts ->
                    if (accounts.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                            item(state.userName) {
                                Text(
                                    state.userName,
                                    Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            items(accounts) { account ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Checkbox(
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = AppColors.AccentColor,
                                        ),
                                        checked = account.isSelected,
                                        onCheckedChange = {
                                            viewModel.onBankAccountSelected(account)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(account.bankAccount.name, modifier = Modifier.weight(1f))
                                    Text(account.bankAccount.currency)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                viewModel.saveUserBankAccounts()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = state.saveUserAccountsButtonEnabled,
                        ) {
                            Text("Save User Accounts")
                        }
                    }
                }
            }
        }
    }
}