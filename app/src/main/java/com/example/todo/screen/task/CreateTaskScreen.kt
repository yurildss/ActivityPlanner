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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.common.SnackbarManager
import com.example.todo.common.SnackbarMessage
import com.example.todo.model.Goals
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
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
    viewModel: CreateTaskScreenViewModel = hiltViewModel(),
    onSaveClick: () -> Unit,
    onCancelTaskClick: () -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarMessage by SnackbarManager.snackbarMessages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let {
            val message = when (it) {
                is SnackbarMessage.StringSnackbar -> it.message
                is SnackbarMessage.ResourceSnackbar -> context.getString(it.message)
            }
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
            }
        }
    }

    val taskuiState by viewModel.CreateTaskUistate
    val golsUiState by viewModel.CreateGolsUistate

    Scaffold(
        modifier = modifier.fillMaxSize().padding(top = 20.dp).testTag("add_task_screen"),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))
            .padding(paddingValues)
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
                modifier = Modifier
                    .fillMaxWidth(0.75f),
                label = { Text("Title",
                    fontFamily = FontFamily.Monospace, color = Color.White)
                        },
                singleLine = true
            )
            OutlinedTextField(
                value = taskuiState.description,
                onValueChange = viewModel::onDescriptionTaskChange,
                modifier = Modifier
                    .fillMaxWidth(0.75f),
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
                onAddGolsClick = viewModel::onAddGoalsClick,
                golsScreenState = golsUiState,
                taskScreenState = taskuiState,
                onDatePickerChange = viewModel::setOpenGolsDatePicker,
                onDateSelected = viewModel::updateGolsDeadLine,
                onDescriptionGoalsChange = viewModel::onDescriptionGolsChange,
                onTimeToCompleteChange = viewModel::onTimeToCompleteGoalsChange,
                onTitleGoalsChange = viewModel::onTitleGolsChange,
                onCreateGols = viewModel::onCreateGoals,
                onGoalsIsSaveChange = viewModel::onGoalsIsSaveChange,
                onDeleteGoalsClick = viewModel::onDeleteGoalsClick,
                onRemoveGoalsClick = viewModel::onRemoveGoalsClick,
                onSaveTaskClick = { viewModel.onSaveTaskClick(onSaveClick) },
                onCancelTaskClick = onCancelTaskClick
            )
        }
    }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ColumnScope.DatePick(
    openDatePicker: Boolean,
    date: String,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit,

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
    val dateState = rememberDatePickerState( initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds())

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
                .fillMaxWidth().testTag("priority_dropdown"),
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
                    text = { Text(option, color = Color.White) },
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
                .fillMaxWidth().testTag("select_a_icon"),
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
    onGoalsIsSaveChange : () -> Unit,
    onDeleteGoalsClick: (Int) -> Unit,
    onRemoveGoalsClick: (Int) -> Unit,
    onSaveTaskClick: () -> Unit,
    onCancelTaskClick: () -> Unit

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
            Button(
                onClick = onAddGolsClick,
                colors = ButtonDefaults.buttonColors(Color(0xFF00FF95)),
                modifier = Modifier.testTag("add_goals_button")
            ) {
                Text("Add Goals")
            }
            LazyColumn(modifier = Modifier
                .weight(1f) // 🔑 Faz a lista ocupar apenas o espaço disponível
                .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            onGoalsIsSaveChange = onGoalsIsSaveChange,
                            onRemoveGoalsClick = onRemoveGoalsClick
                        )
                    }else{
                        GoalsShow(
                            index,
                            goal,
                            onDeleteGoalsClick
                        )
                    }

                }
            }
            Button(
                onClick = onSaveTaskClick,
                modifier = Modifier.fillMaxWidth(0.75f).testTag("save_task_button"),
                colors = ButtonDefaults.buttonColors(Color(0xFF008CBA))
            ) {
                Text("Save")
            }
            Button(
                onClick = onCancelTaskClick,
                colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C)),
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text("Cancel")
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
    onGoalsIsSaveChange: () -> Unit,
    onRemoveGoalsClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C2C3A))
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
                modifier = Modifier
                    .fillMaxWidth(0.75f).testTag("goals_title"),
                label = {
                    Text(
                        "Title",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
            )

            OutlinedTextField(
                createGoalsScreenState.description,
                onValueChange = onDescriptionGoalsChange,
                modifier = Modifier
                    .fillMaxWidth(0.75f).testTag("goals_description"),
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
                createGoalsScreenState.timeToComplete,
                onValueChange = onTimeToCompleteChange,
                modifier = Modifier.testTag("time_to_complete"),
                label = {
                    Text(
                        "Time to complete",
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                },
                singleLine = true,
                placeholder = {
                    Text("in hours")
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                    onCreateGols(index)
                    onGoalsIsSaveChange()
                }, colors = ButtonDefaults.buttonColors(Color(0xFF00FF95)),
                    modifier = Modifier.testTag("add_goals_button") ) {
                    Text("Add")
                }
                Button(
                    onClick = { onRemoveGoalsClick(index) },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C))
                ) {
                    Text("Remove")
                }
            }
        }
    }
}

@Composable
fun GoalsShow(
    index: Int,
    goals: Goals,
    onDeleteGoalsClick: (Int) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C2C3A))
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
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF1D1D2A))
            Text("description: ${goals.description}",
                fontFamily = FontFamily.Monospace,
                maxLines = 2,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF1D1D2A))
            Text(
                goals.dateInBrazilianFormat,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color(0xFF1D1D2A))
            Text(
                "${goals.timeToComplete} hours",
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = { onDeleteGoalsClick(index) },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C))
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
        goals = Goals(),
        index = 1,
        onDeleteGoalsClick = {}
    )
}

@Composable
@Preview
fun AddGoalsCardPreview() {
    AddGoalsCard(
        onAddGolsClick = { },
        golsScreenState = CreateGoalsScreenState(),
        taskScreenState = CreateTaskScreenState(),
        onDatePickerChange = {},
        onDateSelected = {},
        onDescriptionGoalsChange = {},
        onTimeToCompleteChange = {},
        onTitleGoalsChange = {},
        onCreateGols = {},
        onGoalsIsSaveChange = {},
        onDeleteGoalsClick = { },
        onRemoveGoalsClick = {},
        onSaveTaskClick = {},
        onCancelTaskClick = {}
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
        index = 1,
        onGoalsIsSaveChange = {},
        onRemoveGoalsClick = {}
    )
}

@Composable
@Preview
fun CreateTaskScreenPreview() {
    CreateTaskScreen(
        viewModel = TODO(),
        onSaveClick = {},
        modifier = TODO(),
        onCancelTaskClick = TODO()
    )
}

