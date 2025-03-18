package com.example.todo

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.screen.OpenScreen
import com.example.todo.screen.home.HomeScreen
import com.example.todo.screen.login.LoginScreen
import com.example.todo.screen.sign_up.SignUpScreen
import com.example.todo.screen.task.CreateTaskScreen

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
                }
            )
        }

        composable(Screens.SIGN_UP_SCREEN.name){
            SignUpScreen(
                navigateToSingIn = {
                    navController.navigate(Screens.SIGN_IN_SCREEN.name)
                }
            )
        }
        composable(Screens.HOME_SCREEN.name){
            HomeScreen(onAddTaskClick = {
                navController.navigate(Screens.ADD_TASK_SCREEN.name)
            })
        }

        composable(Screens.ADD_TASK_SCREEN.name){
            CreateTaskScreen()
        }
    }
}