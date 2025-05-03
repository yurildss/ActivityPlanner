package com.example.todo.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun UserScreen(viewModel: UserScreenViewModel = hiltViewModel()){

    val uiState by viewModel.uiState

    Box(Modifier
        .fillMaxSize()
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
                    Text("Name")
                },
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                label = {
                    Text("Email")
                },
                readOnly = true,
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                label = {
                    Text("Password")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = uiState.repeatPassword,
                onValueChange = {
                    viewModel.onRepeatPasswordChange(it)
                },
                label = {
                    Text("Repeat password")
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Button(onClick = {
                viewModel.onNewPasswordChange(uiState.password)
            }) {
                Text("Change password")
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.75f))
            Row{
                Button(onClick = {}) {
                    Text("Save")
                }
                Button(onClick = {}) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
fun UserScreenPreview(){
    UserScreen()
}