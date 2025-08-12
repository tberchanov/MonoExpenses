package com.monoexpenses.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.monoexpenses.presentation.accounts.configuration.ui.AccountsConfigurationScreen
import com.monoexpenses.presentation.categories.configuration.ui.CategoriesConfigurationScreen
import com.monoexpenses.presentation.home.ui.HomeScreen
import com.monoexpenses.presentation.ui.theme.MonoExpensesTheme

private const val CATEGORIES_MODIFIED_KEY = "CATEGORIES_MODIFIED_KEY"

enum class Screen {
    Home,
    CategoriesConfiguration(),
    AccountsConfiguration(),
}

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No Snackbar Host State")
}

@Composable
fun AppUI(
    navController: NavHostController = rememberNavController(),
    onNavHostReady: suspend (NavController) -> Unit = {},
) {
    MonoExpensesTheme() {
        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    composable(route = Screen.Home.name) { entry ->
                        val isCategoriesModified =
                            entry.savedStateHandle.get<Boolean>(CATEGORIES_MODIFIED_KEY) ?: false
                        if (isCategoriesModified) {
                            entry.savedStateHandle[CATEGORIES_MODIFIED_KEY] = false
                        }
                        HomeScreen(
                            isCategoriesModified = isCategoriesModified,
                            onCategoriesSettingsClicked = {
                                navController.navigate(Screen.CategoriesConfiguration.name)
                            },
                            onAddBankAccountsClicked = {
                                navController.navigate(Screen.AccountsConfiguration.name)
                            }
                        )
                    }
                    composable(route = Screen.CategoriesConfiguration.name) {
                        CategoriesConfigurationScreen(
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onCategoriesModified = {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(CATEGORIES_MODIFIED_KEY, true)
                            },
                        )
                    }
                    composable(route = Screen.AccountsConfiguration.name) {
                        AccountsConfigurationScreen(onBackClicked = {
                            navController.popBackStack()
                        })
                    }
                }
            }
        }
        LaunchedEffect(navController) {
            onNavHostReady(navController)
        }
    }
}