package com.example.todo.screen.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                label = {Text("Description", color = Color.White)},
            )
            DatePick(onDatePickerChange, onDateSelected)
            DropDownMenuSample()
            IconPickerDropdown()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ColumnScope.DatePick(
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent
        ),
        value = "",
        onValueChange = {},
        label = {
            Text(
                text = "DeadLine",
                color = Color.White
            )
        },
        interactionSource = remember {
            MutableInteractionSource()
        }.also { interections ->
            LaunchedEffect(interections) {

                interections.interactions.collectLatest {

                    if (it is PressInteraction.Release) {
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
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                    onClick = {

                        dateState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.UTC).date.format(LocalDate.Format {
                                    byUnicodePattern("dd/MM/yyyy")
                                })

                            onDateSelected(selectedDate)
                            onDatePickerChange(false)
                        }

                    }) {
                    Text("Selecionar")
                }
            }) {
            DatePicker(state = dateState)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuSample() {
    val options = listOf("-"," Priority 1", "Priority 2", "Priority 3")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(0.75f).padding(top = 5.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = {
                Text("Select the priority",
                color = Color.White
            )
                    },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown,
                    contentDescription = "Abrir menu")
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconPickerDropdown() {
    val icons = listOf(
        Icons.Default.Add to "",
        Icons.Default.Home to "Home",
        Icons.Default.Star to "Star",
        Icons.Default.Person to "Person",
        Icons.Default.Settings to "Settings",
        Icons.Default.Email to "Email",
        Icons.Default.Info to "Info",
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf(icons[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(0.75f).padding(top = 5.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedIcon.second, // Nome do ícone
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text("Select a icon", color = Color.White) },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(selectedIcon.first, contentDescription = selectedIcon.second)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir menu")
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            icons.forEach { icon ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(icon.first, contentDescription = icon.second)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(icon.second)
                        }
                    },
                    onClick = {
                        selectedIcon = icon
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddGoalsCard(){
    Box(modifier = Modifier
        .fillMaxWidth()
    ){
        Column(Modifier.fillMaxWidth() ,horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { /*TODO*/ }) {
                Text("Add Goals")
            }

            LazyColumn {

            }
        }
    }
}

@Composable
fun GoalsEntry(onDatePickerChange: (Boolean) -> Unit,
               onDateSelected: (String) -> Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(Color(0xFF498374)).padding(10.dp)
    ){
        Column(Modifier.fillMaxWidth() ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            OutlinedTextField("",
                onValueChange = {},
                label = {Text("Title",
                    color = Color.White)},
                singleLine = true
            )

            OutlinedTextField("",
                onValueChange = {},
                label = {Text("Description",
                    color = Color.White)}
            )
            DatePick(onDatePickerChange, onDateSelected)
            OutlinedTextField("",
                onValueChange = {},
                label = {Text("Time to complete",
                    color = Color.White)},
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(0.75f).padding(10.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = { /*TODO*/ }) { Text("Add") }
                Button(onClick = { /*TODO*/ }) { Text("Remove") }
            }
        }
    }
}

@Composable
@Preview
fun AddGoalsCardPreview(){
    AddGoalsCard()
}

@Composable
@Preview
fun GoalsEntryPreview(){
    GoalsEntry(
        onDatePickerChange = {},
        onDateSelected = {}
    )
}

@Composable
@Preview
fun CreateTaskScreenPreview(){
    CreateTaskScreen(

        onDatePickerChange = {},
        onDateSelected = {}
    )
}