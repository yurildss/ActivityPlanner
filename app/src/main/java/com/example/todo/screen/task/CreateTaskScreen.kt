package com.example.todo.screen.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
    viewModel: CreateTaskScreenViewModel = hiltViewModel()
) {

    val taskuiState by viewModel.CreateTaskUistate
    val golsUiState by viewModel.CreateGolsUistate

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF6EA68E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = taskuiState.title,
                onValueChange = viewModel::onTitleTaskChange,
                label = { Text("Title",
                    fontFamily = FontFamily.Monospace, color = Color.White) },
                singleLine = true
            )
            OutlinedTextField(
                value = taskuiState.description,
                onValueChange = viewModel::onDescriptionTaskChange,
                label = { Text("Description",
                    fontFamily = FontFamily.Monospace, color = Color.White) },
            )
            DatePick(
                taskuiState.openDatePicker,
                taskuiState.deadLine,
                viewModel::setOpenDatePicker,
                viewModel::updateTaskDeadLine
            )
            DropDownMenuSample(
                options = viewModel.options,
                expanded = viewModel.expandedPriority.value,
                onExpandedChange = viewModel::onExpandedPriorityChange,
                selectedOption = viewModel.selectedPriorityOption.value,
                onPriorityTaskChange = viewModel::onPriorityTaskChange
            )
            IconPickerDropdown(
                icons = viewModel.icons,
                expanded = viewModel.expandedIcon.value,
                onExpandedChange = viewModel::onExpandedIconChange,
                selectedIcon = viewModel.selectedIcon.value,
                onSelectedIconChange = viewModel::onSelectedIconChange
            )
            AddGoalsCard(
                onAddGolsClick = viewModel::onAddGolsClick,
                golsScreenState = golsUiState,
                taskScreenState = taskuiState,
                onDatePickerChange = viewModel::setOpenGolsDatePicker,
                onDateSelected = viewModel::updateGolsDeadLine,
                onDescriptionGoalsChange = viewModel::onDescriptionGolsChange,
                onTimeToCompleteChange = viewModel::onTimeToCompleteGoalsChange,
                onTitleGoalsChange = viewModel::onTitleGolsChange,
                onCreateGols = viewModel::onCreateGoals,
                onGoalsIsSaveChange = viewModel::onGoalsIsSaveChange
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ColumnScope.DatePick(
    openDatePicker: Boolean,
    date: String,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit
) {
    TextField(
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent
        ),
        value = date,
        onValueChange = {},
        label = {
            Text(
                text = "DeadLine",
                color = Color.White,
                fontFamily = FontFamily.Monospace
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

    AnimatedVisibility(visible = openDatePicker) {
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
                    Text("Select")
                }
            }) {
            DatePicker(state = dateState)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuSample(
    options: List<String>,
    selectedOption: String,
    expanded: Boolean,
    onPriorityTaskChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit
) {

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 5.dp),
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = {
                Text(
                    "Select the priority",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Abrir menu"
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onPriorityTaskChange(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconPickerDropdown(
    icons: List<Pair<ImageVector, String>>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedIcon: Pair<ImageVector, String>,
    onSelectedIconChange: (Pair<ImageVector, String>) -> Unit,
) {

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 5.dp),
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) }
    ) {
        OutlinedTextField(
            value = selectedIcon.second, // Nome do ícone
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = { Text("Select a icon", color = Color.White, fontFamily = FontFamily.Monospace) },
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
            onDismissRequest = { onExpandedChange(false) }
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
                        onSelectedIconChange(icon)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun AddGoalsCard(
    onAddGolsClick: () -> Unit,
    golsScreenState: CreateGoalsScreenState,
    taskScreenState: CreateTaskScreenState,
    onDatePickerChange: (Boolean) -> Unit,
    onDescriptionGoalsChange: (String) -> Unit,
    onTimeToCompleteChange: (String) -> Unit,
    onTitleGoalsChange: (String) -> Unit,
    onCreateGols: (Int) -> Unit,
    onDateSelected: (String) -> Unit,
    onGoalsIsSaveChange : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onAddGolsClick) {
                Text("Add Goals")
            }
            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                itemsIndexed(taskScreenState.gols) { index, goal ->
                    if (!goal.isSave) {
                        GoalsEntry(
                            index = index,  // Agora passamos o índice correto
                            createGoalsScreenState = golsScreenState,
                            onDatePickerChange = onDatePickerChange,
                            onDateSelected = onDateSelected,
                            onGoalsTaskChange = onTitleGoalsChange,
                            onDescriptionGoalsChange = onDescriptionGoalsChange,
                            onTimeToCompleteChange = onTimeToCompleteChange,
                            onCreateGols = onCreateGols,
                            onGoalsIsSaveChange = onGoalsIsSaveChange
                        )
                    }else{
                        GoalsShow(golsScreenState)
                    }
                }
            }

        }
    }
}

@Composable
fun GoalsEntry(
    index: Int,
    onGoalsTaskChange: (String) -> Unit,
    onDescriptionGoalsChange: (String) -> Unit,
    onTimeToCompleteChange: (String) -> Unit,
    createGoalsScreenState: CreateGoalsScreenState,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit,
    onCreateGols: (Int) -> Unit,
    onGoalsIsSaveChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF498374))
            .padding(10.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            OutlinedTextField(
                createGoalsScreenState.title,
                onValueChange = onGoalsTaskChange,
                label = {
                    Text(
                        "Title",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                },
                singleLine = true
            )

            OutlinedTextField(
                createGoalsScreenState.description,
                onValueChange = onDescriptionGoalsChange,
                label = {
                    Text(
                        "Description",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                }
            )
            DatePick(
                createGoalsScreenState.openGoalsDatePicker,
                createGoalsScreenState.deadLine, onDatePickerChange,
                onDateSelected
            )
            OutlinedTextField(
                createGoalsScreenState.timeToComplete.toString(),
                onValueChange = onTimeToCompleteChange,
                label = {
                    Text(
                        "Time to complete",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                },
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    onCreateGols(index)
                    onGoalsIsSaveChange()
                } ) { Text("Add") }
                Button(onClick = { /*TODO*/ }) { Text("Remove") }
            }
        }
    }
}

@Composable
fun GoalsShow(goals: CreateGoalsScreenState){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF498374))
            .padding(10.dp)
    ) {
        Column(Modifier.fillMaxWidth(),) {
            Text("Title: ${goals.title}",
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF386459))
            Text("description: ${goals.description}",
                fontFamily = FontFamily.Monospace,
                maxLines = 2,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF386459))
            Text(
                goals.deadLine,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF386459))
            Text(
                goals.timeToComplete,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {  },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Delete")
            }
        }
    }
}

@Composable
@Preview
fun GoalsShowPreview() {
    GoalsShow(
        goals = CreateGoalsScreenState()
    )
}

@Composable
@Preview
fun AddGoalsCardPreview() {
    AddGoalsCard(
        onAddGolsClick = TODO(),
        golsScreenState = TODO(),
        taskScreenState = TODO(),
        onDatePickerChange = TODO(),
        onDateSelected = TODO(),
        onDescriptionGoalsChange = TODO(),
        onTimeToCompleteChange = TODO(),
        onTitleGoalsChange = TODO(),
        onCreateGols = TODO(),
        onGoalsIsSaveChange = TODO()
    )
}

@Composable
@Preview
fun GoalsEntryPreview() {
    GoalsEntry(
        onDatePickerChange = {},
        onDateSelected = {},
        createGoalsScreenState = CreateGoalsScreenState(),
        onGoalsTaskChange = { },
        onDescriptionGoalsChange = { },
        onTimeToCompleteChange = { },
        onCreateGols = { index: Int -> },
        index = TODO(),
        onGoalsIsSaveChange = TODO()
    )
}

@Composable
@Preview
fun CreateTaskScreenPreview() {
    CreateTaskScreen()
}

