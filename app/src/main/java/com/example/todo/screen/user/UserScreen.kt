package com.example.todo.screen.user

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo.Screens
import com.example.todo.common.SnackbarManager
import com.example.todo.common.SnackbarMessage
import com.example.todo.screen.home.BottomMenu
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: UserScreenViewModel = hiltViewModel()
){

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by viewModel.uiState
    val currentRoute = navController.currentBackStackEntry?.destination?.route


    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            val message = when (it) {
                is SnackbarMessage.StringSnackbar -> it.message
                is SnackbarMessage.ResourceSnackbar -> context.getString(it.message)
            }
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("user_screen"),
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ) { @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
        Box(Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .background(Color(0xFF1D1D2A))
        ){
            Column(Modifier
                .fillMaxSize()
                .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = {
                        viewModel.onNameChange(it)
                    },
                    label = {
                        Text("Name", color = Color.White)
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = {
                    },
                    label = {
                        Text("Email", color = Color.White)
                    },
                    readOnly = true,
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                OutlinedTextField(
                    value = uiState.oldPassword,
                    onValueChange = {
                        viewModel.onOldPasswordChange(it)
                    },
                    label = {
                        Text("Password", color = Color.White)
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                    },
                    label = {
                        Text("Password", color = Color.White)
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                OutlinedTextField(
                    value = uiState.repeatPassword,
                    onValueChange = {
                        viewModel.onRepeatPasswordChange(it)
                    },
                    label = {
                        Text("Repeat password", color = Color.White)
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                    )
                )
                Button(onClick = {
                    viewModel.onNewPasswordChange(uiState.password)
                },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(300.dp, 50.dp).testTag("change_password_profile_button"),
                    colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))) {
                    Text("Change password",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.75f))
                Button(onClick =
                    viewModel::onSaveButtonClick
                ,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(350.dp, 50.dp).testTag("edit_profile_button"),
                    colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))
                ) {
                    Text("Save",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace)
                }
                BottomMenu(
                    currentRoute = currentRoute,
                    onNavigationSelected = {
                        if(currentRoute != it){
                            navController.navigate(it){
                                popUpTo(Screens.HOME_SCREEN.name){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun UserScreenPreview(){
    UserScreen(
        navController = TODO(),
        modifier = TODO(),
        viewModel = TODO()
    )
}