package com.example.todo.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.common.SnackbarManager
import com.example.todo.common.SnackbarMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    signUp: () -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState

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
        modifier = modifier.fillMaxSize().testTag("login_screen"),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6EA68E))
                .padding(paddingValues)
        ) {
            Column(
                Modifier.fillMaxSize().padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Email",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor =  Color(0xFF9FD7B0),
                        unfocusedBorderColor = Color(0xFFA8D5BA))
                    )
                OutlinedTextField(
                    value = uiState.password,
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Password",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor =  Color(0xFF9FD7B0),
                        unfocusedBorderColor = Color(0xFFA8D5BA),)
                )
                Text(
                    text = "Forgot Password?",
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Start)
                )
                Button(
                    onClick = { viewModel.onSignInClick(navigateToHome) },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(350.dp, 50.dp).testTag("login_button"),
                    colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))
                ) {
                    Text("Login", color = Color.Black, fontSize = 20.sp)
                }
                Button(
                    onClick = signUp,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(350.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))
                ) {
                    Text("Sign Up", color = Color.Black, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
@Preview
fun LoginScreenPreview(){

}