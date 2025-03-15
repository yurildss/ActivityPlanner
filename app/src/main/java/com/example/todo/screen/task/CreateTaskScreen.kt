package com.example.todo.screen.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    modifier: Modifier = Modifier,
    onDatePickerChange: (Boolean) -> Unit, // Passar controle do DatePicker,
    onDateSelected: (String) -> Unit,
    ){

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color(0xFF6EA68E))
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                "",
                onValueChange = {},
                label = {Text("Title", color = Color.White)},
                singleLine = true
            )
            OutlinedTextField(
                "",
                onValueChange = {},
                label = {Text("Descrição", color = Color.White)},
            )
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent),
                value = "",
                onValueChange = {},
                label = {
                    Text(text = "DeadLine",
                        color = Color.White)
                }   ,
                interactionSource = remember {
                    MutableInteractionSource()
                }.also { interections ->
                    LaunchedEffect(interections) {

                        interections.interactions.collectLatest {

                            if(it is PressInteraction.Release){
                                onDatePickerChange(true)
                            }

                        }
                    }
                },
                readOnly = true
            )
            val dateState = rememberDatePickerState()

            AnimatedVisibility(visible = false) {
                DatePickerDialog(
                    onDismissRequest = {
                        onDatePickerChange(false)
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary) ,onClick = {

                                dateState.selectedDateMillis?.let {   millis->
                                    val selectedDate =  Instant.fromEpochMilliseconds(millis)
                                        .toLocalDateTime(TimeZone.UTC).date.format(LocalDate.Format {
                                            byUnicodePattern("dd/MM/yyyy")
                                        })

                                    onDateSelected(selectedDate)
                                    onDatePickerChange(false)
                                }

                            }) {
                            Text("Selecionar")
                        } }) {
                    DatePicker(state = dateState)
                }

            }
        }
    }
}


@Composable
@Preview
fun CreateTaskScreenPreview(){
    CreateTaskScreen(

        onDatePickerChange = {},
        onDateSelected = {}
    )
}