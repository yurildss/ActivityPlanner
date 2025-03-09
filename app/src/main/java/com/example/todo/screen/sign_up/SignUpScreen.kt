package com.example.todo.screen.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpScreenViewModel = hiltViewModel(),
    navigateToSingIn: () -> Unit
){

    val uiState by viewModel.uiState

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color(0xFF6EA68E))){
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") }
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange =  viewModel::onPasswordChange  ,
                label = { Text("Password") }
            )
            OutlinedTextField(
                value = uiState.repeatPassword,
                onValueChange = viewModel::onRepeatPasswordChange,
                label = { Text("Repeat password") }
            )
            Button(
                onClick = { viewModel.onSignUpClick(navigateToSingIn) },
                modifier = Modifier
                    .padding(5.dp)
                    .size(350.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))
            ){
                Text("Sign Up", color = Color.Black, fontSize = 20.sp)

            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(5.dp)
                    .size(350.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFB2F02C))
            ){
                Text("Login", color = Color.Black, fontSize = 20.sp)
            }
        }
    }
}

@Composable
@Preview
fun SignUpScreenPreview(){

}