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

@Composable
fun ToDoApp(navController: NavHostController = rememberNavController()){

    NavHost(
        navController = navController,
        startDestination = Screens.OPEN_SCREEN.name
    ){
        composable(Screens.OPEN_SCREEN.name){
            OpenScreen(onGetStarted = {
                navController.navigate(Screens.LOGIN_SCREEN.name)
            })
        }
        composable(Screens.LOGIN_SCREEN.name){
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Screens.HOME_SCREEN.name){
                        popUpTo(Screens.LOGIN_SCREEN.name){
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screens.SIGN_UP_SCREEN.name){
            SignUpScreen(
                navigateToSingIn = {
                    navController.navigate(Screens.LOGIN_SCREEN.name)
                }
            )
        }
        composable(Screens.HOME_SCREEN.name){
            HomeScreen()

        }

    }
}