package com.example.actividad_finalandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad_finalandroid.domain.usecase.auth.GetCurrentUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LoginUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.LogoutUserUseCase
import com.example.actividad_finalandroid.domain.usecase.auth.RegisterUserUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.DeleteDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.GetDraftsUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.PublishDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.draft.SaveDraftUseCase
import com.example.actividad_finalandroid.domain.usecase.task.CreateTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.DeleteTaskUseCase
import com.example.actividad_finalandroid.domain.usecase.task.GetTasksUseCase
import com.example.actividad_finalandroid.domain.usecase.task.UpdateTaskUseCase
import com.example.actividad_finalandroid.ui.screen.DraftsScreen
import com.example.actividad_finalandroid.ui.screen.LoginScreen
import com.example.actividad_finalandroid.ui.screen.RegisterScreen
import com.example.actividad_finalandroid.ui.screen.TaskScreen
import com.example.actividad_finalandroid.ui.state.DraftViewModel
import com.example.actividad_finalandroid.ui.state.LoginViewModel
import com.example.actividad_finalandroid.ui.state.RegisterViewModel
import com.example.actividad_finalandroid.ui.state.TaskViewModel

@Composable
fun NavGraph(
    registerUserUseCase: RegisterUserUseCase,
    loginUserUseCase: LoginUserUseCase,
    logoutUserUseCase: LogoutUserUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    createTaskUseCase: CreateTaskUseCase,
    getTasksUseCase: GetTasksUseCase,
    updateTaskUseCase: UpdateTaskUseCase,
    deleteTaskUseCase: DeleteTaskUseCase,
    getDraftsUseCase: GetDraftsUseCase,
    saveDraftUseCase: SaveDraftUseCase,
    deleteDraftUseCase: DeleteDraftUseCase,
    publishDraftUseCase: PublishDraftUseCase,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Automatic redirection check
    val isUserLoggedIn = getCurrentUserUseCase() != null
    val startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val loginViewModel = androidx.compose.runtime.remember {
                LoginViewModel(loginUserUseCase, getCurrentUserUseCase)
            }
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val registerViewModel = androidx.compose.runtime.remember {
                RegisterViewModel(registerUserUseCase)
            }
            RegisterScreen(
                viewModel = registerViewModel,
                onRegistrationSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            val taskViewModel = androidx.compose.runtime.remember {
                TaskViewModel(
                    createTaskUseCase = createTaskUseCase,
                    getTasksUseCase = getTasksUseCase,
                    updateTaskUseCase = updateTaskUseCase,
                    deleteTaskUseCase = deleteTaskUseCase,
                    saveDraftUseCase = saveDraftUseCase,
                    getDraftsUseCase = getDraftsUseCase,
                    publishDraftUseCase = publishDraftUseCase,
                    getCurrentUserUseCase = getCurrentUserUseCase
                )
            }
            TaskScreen(
                viewModel = taskViewModel,
                onLogoutClick = {
                    logoutUserUseCase()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToDrafts = {
                    navController.navigate(Screen.Drafts.route)
                }
            )
        }

        composable(Screen.Drafts.route) {
            val draftViewModel = androidx.compose.runtime.remember {
                DraftViewModel(
                    getDraftsUseCase = getDraftsUseCase,
                    saveDraftUseCase = saveDraftUseCase,
                    deleteDraftUseCase = deleteDraftUseCase,
                    publishDraftUseCase = publishDraftUseCase,
                    getCurrentUserUseCase = getCurrentUserUseCase
                )
            }
            DraftsScreen(
                viewModel = draftViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
