package com.example.todo.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.model.Task
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAddTaskClick: () -> Unit,
    onTaskClick: (String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
){

    val uiState = viewModel.uiState.value
    val tasks = uiState.tasksOfTheDay.collectAsStateWithLifecycle(emptyList())

    Box(contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxSize()
            .background(
                Color(0xFF1D1D2A)
            )
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            UserHomeScreen(uiState)
            SettingsPart(
                openDatePicker = uiState.openDatePicker,
                date = uiState.actualDay,
                onDatePickerChange = viewModel::setOpenDatePicker,
                onDateSelected = viewModel::updateTaskDeadLine,
                onAddTaskClick = onAddTaskClick,
                actualDay = uiState.actualDay
            )
            LazyColumn(
                Modifier
                    .padding(10.dp)
                    .fillMaxSize(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                if (tasks.value.isEmpty()) {
                    item{
                        Text(text = "No tasks end in this day",
                            color = Color.White,
                            fontSize = 35.sp,
                        )
                    }
                }
                else{
                    items(items = tasks.value, key = { it.id }) { taskItem ->
                        TaskCard(taskItem, onTaskClick)
                    }
                }
            }
        }
        BottomMenu()
    }
}

@Composable
private fun BottomMenu() {
    NavigationBar(
        containerColor = Color.Unspecified
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* Navegar */ },
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFB4EF2C)
            ),
            label = { }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Navegar */ },
            icon = {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Setting",
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFB4EF2C)),
            label = { }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Navegar */ },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White
                )
            },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFB4EF2C)),
            label = { }
        )
    }
}

@Composable
fun UserHomeScreen(uiState: HomeScreenUiState){
    Column(Modifier
        .fillMaxWidth()
        .padding(10.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xA320E1FF))
                    .padding(16.dp)
            ){
                Icon(Icons.Default.AccountCircle, null)
            }
            Column(Modifier.padding(start = 16.dp)) {
                Text("Hello",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,)
                Text(
                    "${uiState.name}!", color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0x166EA68E))
                    .padding(16.dp)
            ){
                Icon(Icons.Default.Search,
                    null,
                    tint = Color.White
                )
            }
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0x166EA68E))
                    .padding(16.dp)
            ){
                Icon(Icons.Default.Notifications,
                    null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Activity", color = Color.White,
            fontSize = 60.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(top = 10.dp, start = 10.dp))
        Text("Planner",
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 60.sp)

        Row(verticalAlignment = Alignment.CenterVertically){
            Row(
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0x166EA68E)
                    )
                    .padding(5.dp)
            ){
                Icon(
                    Icons.Default.Info,
                    null,
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Text("You have ${uiState.pendingTask} pending tasks today",
                color = Color.White,
                modifier = Modifier.padding(start = 5.dp))
        }
    }
}

@Composable
fun SettingsPart(
    actualDay: String,
    openDatePicker: Boolean,
    date: String,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit,
    onAddTaskClick: () -> Unit
){
    Row(Modifier
        .fillMaxWidth()
        .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0x166EA68E))
                .padding(16.dp)
        ){
            Icon(Icons.Default.Menu,
                null,
                tint = Color.White
            )
        }
        Row(
            Modifier
                .size(150.dp, 54.dp)
                .clip(CircleShape)
                .background(Color(0xFFB4EF2C))
                .padding(16.dp)
        ){
            Icon(Icons.Default.DateRange,
                null,
                tint = Color.White
            )
            DatePick(
                openDatePicker = openDatePicker,
                date = date,
                onDatePickerChange = onDatePickerChange,
                onDateSelected = onDateSelected,
                actualDay = actualDay
            )
        }
        Row(
            Modifier
                .size(150.dp, 54.dp)
                .clip(CircleShape)
                .background(Color(0x166EA68E))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = onAddTaskClick,
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Icon(Icons.Default.Add,
                    null,
                    tint = Color.White
                )
                Text("Add Task",
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.White)
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onTaskClick: (String) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f) // 50% da largura da tela
        .fillMaxHeight(0.20f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFB4EF2C))
            .clickable {
                onTaskClick(task.id)
            }
            .padding(15.dp) // 25% da altura da tela)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF242636))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Default.DateRange,
                    null,
                    tint = Color(0xFFB4EF2C)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(task.title,
                fontSize = 50.sp,
                color = Color(0xFF242636)
            )

            Text(task.timeToComplete.toString(),
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                color = Color((0xFF242636)))

            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { 24f / 100f },
                    modifier = Modifier.fillMaxWidth(0.75f),
                    color = Color(0xFF242636),
                    trackColor = Color(0xFF90C323),
                )
                Text("24%",
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color(0xFF242636))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DatePick(
    actualDay: String,
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
                text = actualDay,
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

@Composable
@Preview
fun TaskCardPreview(){
    TaskCard(
        Task(),
        onTaskClick = TODO()
    )
}

@Composable
@Preview
fun SettingsPartPreview(){
    SettingsPart(
        "",
        date = TODO(),
        onDatePickerChange = TODO(),
        onDateSelected = TODO(),
        onAddTaskClick = TODO(),
        openDatePicker = TODO()
    )
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        onAddTaskClick = { },
        modifier = TODO(),
        onTaskClick = TODO(),
        viewModel = TODO(),
    )
}

@Composable
@Preview
fun UserHomeScreenPreview(){
    UserHomeScreen(
        uiState = TODO()
    )
}