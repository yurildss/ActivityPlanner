package com.example.todo

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo.screen.OpenScreen
import com.example.todo.screen.home.HomeScreen
import com.example.todo.screen.login.LoginScreen
import com.example.todo.screen.notifications.NotificationScreen
import com.example.todo.screen.recovery.RecoveryPasswordScreen
import com.example.todo.screen.sign_up.SignUpScreen
import com.example.todo.screen.task.CreateTaskScreen
import com.example.todo.screen.task.TaskInfo
import com.example.todo.screen.task.ViewCompletedTasks
import com.example.todo.screen.task.ViewDelayTasks
import com.example.todo.screen.user.UserScreen

@Composable
fun ToDoApp(navController: NavHostController = rememberNavController()){

    NavHost(
        navController = navController,
        startDestination = Screens.OPEN_SCREEN.name
    ){
        composable(Screens.OPEN_SCREEN.name){
            OpenScreen(onGetStarted = {
                navController.navigate(Screens.SIGN_IN_SCREEN.name)
            })
        }

        composable(Screens.SIGN_IN_SCREEN.name){
            LoginScreen(signUp = {navController.navigate(Screens.SIGN_UP_SCREEN.name)},
                navigateToHome = {
                    navController.navigate(Screens.HOME_SCREEN.name){
                        popUpTo(Screens.SIGN_UP_SCREEN.name){
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(Screens.RECOVERY_SCREEN.name)
                }
            )
        }

        composable(Screens.RECOVERY_SCREEN.name) {
            RecoveryPasswordScreen(
                onEmailSend = {
                    navController.navigate(Screens.SIGN_IN_SCREEN.name)
                }
            )
        }

        composable(Screens.SIGN_UP_SCREEN.name){
            SignUpScreen(
                navigateToSingIn = {
                    navController.navigate(Screens.SIGN_IN_SCREEN.name)
                },
                onLoginClick = {
                    navController.navigate(Screens.SIGN_IN_SCREEN.name)
                }
            )
        }
        composable(Screens.HOME_SCREEN.name){
            HomeScreen(
                onAddTaskClick = {
                    navController.navigate(Screens.ADD_TASK_SCREEN.name)
                },
                onTaskClick = {
                    navController.navigate("${Screens.TASK_SCREEN.name}/$it")
                },
                onNotificationClick = {
                    navController.navigate(Screens.NOTIFICATION_SCREEN.name)
                },
                onLateTaskClick = {
                    navController.navigate(Screens.DELAY_TASK_SCREEN.name)
                },
                onCompletedTaskClick = {
                    navController.navigate(Screens.COMPLETED_TASK_SCREEN.name)
                },
                onSearchTaskClick = {
                    navController.navigate(Screens.SEARCH_SCREEN.name)
                },
                navController = navController,
            )
        }

        composable(Screens.SEARCH_SCREEN.name){

        }

        composable(Screens.TASK_SCREEN.name+"/{taskId}",
            arguments = listOf(navArgument("taskId"){
                type = NavType.StringType
            })
        ){
            TaskInfo(onBackClick = {
                navController.navigate(Screens.HOME_SCREEN.name){
                    popUpTo(Screens.HOME_SCREEN.name){
                        inclusive = true
                    }
                }
            })
        }

        composable(Screens.ADD_TASK_SCREEN.name){
            CreateTaskScreen(
                onSaveClick = {
                    navController.navigate(Screens.HOME_SCREEN.name)
                },
                onCancelTaskClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screens.NOTIFICATION_SCREEN.name){
            NotificationScreen(
                onTaskClick = {
                    navController.navigate("${Screens.TASK_SCREEN.name}/$it")
                },
                onArrowBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(Screens.DELAY_TASK_SCREEN.name){
            ViewDelayTasks()
        }

        composable(Screens.COMPLETED_TASK_SCREEN.name) {
            ViewCompletedTasks()
        }

        composable(Screens.USER_PROFILE_SCREEN.name){
            UserScreen(
                navController = navController
            )
        }
    }
}