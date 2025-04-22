package com.example.todo.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.model.Task
import kotlinx.coroutines.Delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    onNotificationClick: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
){

    val uiState by viewModel.uiState
    val tasks = uiState.tasksOfTheDay

    Box(contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxSize().padding(top = 20.dp)
            .background(
                Color(0xFF1D1D2A)
            )
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            UserHomeScreen(uiState, onNotificationClick)
            SettingsPart(
                openDatePicker = uiState.openDatePicker,
                date = uiState.actualDay,
                onDatePickerChange = viewModel::setOpenDatePicker,
                onDateSelected = viewModel::updateTaskDeadLine,
                onAddTaskClick = onAddTaskClick,
                actualDay = uiState.actualDay
            )
            TasksList(tasks, onTaskClick)
        }
        BottomMenu()
    }
}

@Composable
private fun TasksList(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
    LazyColumn(
        Modifier
            .padding(10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (tasks.isEmpty()) {
            item {
                Text(
                    text = "No tasks end in this day",
                    color = Color.White,
                    fontSize = 35.sp,
                )
            }
        } else {
            items(tasks.chunked(2)) { rowTasks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (task in rowTasks) {
                        if(task.isOverdue){
                            DelayTaskCard(
                                task = task,
                                onTaskClick = onTaskClick,
                                Modifier
                                    .weight(1f) //Divida o espaço disponível igualmente entre os itens com o mesmo peso
                                    .fillMaxHeight(0.25f) // Ajuste a altura que quiser)
                            )
                        }else{
                            TaskCard(
                                task = task,
                                onTaskClick = onTaskClick,
                                Modifier
                                    .weight(1f) //Divida o espaço disponível igualmente entre os itens com o mesmo peso
                                    .fillMaxHeight(0.25f) // Ajuste a altura que quiser
                            )
                        }
                    }

                    // Se só tiver 1 task nesse row, adiciona um espaço vazio pra alinhar corretamente
                    if (rowTasks.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
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
fun UserHomeScreen(uiState: HomeScreenUiState, onNotificationClick: () -> Unit = {}){
    Column(Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xA320E1FF))
                    .padding(16.dp), horizontalArrangement = Arrangement.End
            ){
                Icon(Icons.Default.AccountCircle, null)
            }
            Column(
                Modifier
                .padding(start = 16.dp)
                .fillMaxWidth(0.6f)
            ) {
                Text("Hello,",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                    )
                Text(
                    "${uiState.name}!", color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
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
            if (uiState.notifications>0){
                Row(
                    Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .padding(16.dp).clickable {
                            onNotificationClick()
                        }
                ){
                    Icon(Icons.Default.Notifications,
                        null,
                        tint = Color.White
                    )
                }
            }else{
                Row(
                    Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x166EA68E))
                        .padding(16.dp).clickable {
                            onNotificationClick()
                        }
                ){
                    Icon(Icons.Default.Notifications,
                        null,
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Activity", color = Color.White,
            fontSize = 60.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(top = 10.dp, start = 10.dp))
        Text("Planner",
            color = Color.White,
            fontFamily = FontFamily.Monospace,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPart(
    actualDay: String,
    openDatePicker: Boolean,
    date: String,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit,
    onAddTaskClick: () -> Unit
){

    val dateState = rememberDatePickerState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState =  drawerState,
        drawerContent = {
            DrawerContent()
        },
    ) {
        Row(Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0x166EA68E))
                    .padding(16.dp).clickable {
                        scope.launch {
                            drawerState.open()
                        }
                    }
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
                    actualDay = actualDay,
                    dateState = dateState
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
}

@Composable
fun TaskCardWithoutGoals(task: Task, onTaskClick: (String) -> Unit, modifier: Modifier = Modifier){

}

@Composable
fun DelayTaskCard(
    task: Task,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    val sizeOfGoals = task.goals.size
    val sizeOfUncompletedGoals = task.goals.count { !it.completed }
    val sizeOfCompletedGoals = task.goals.count { it.completed }
    val progress = sizeOfCompletedGoals.toFloat() / sizeOfGoals.toFloat()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFD64545))
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
                    .background(Color(0xFFFF6B6B))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(Icons.Default.DateRange,
                    null,
                    tint = Color(0xFFFFECEC)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = task.title,
                fontSize = 25.sp,
                color = Color(0xFFF5F5F5),
                maxLines = 2, // ou 2 se quiser permitir mais linhas
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text ="${task.timeToComplete} Hours Needed",
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                color = Color((0xFFF5F5F5))
            )

            Row(Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(0.70f),
                    color = Color(0xFFFFFFFF),
                    trackColor = Color(0xFFFFD93D),
                )
                Text("${progress*100}%",
                    color = Color(0xFFF5F5F5))
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (String) -> Unit, modifier: Modifier = Modifier
){

    val sizeOfGoals = task.goals.size
    val sizeOfUncompletedGoals = task.goals.count { !it.completed }
    val sizeOfCompletedGoals = task.goals.count { it.completed }
    val progress = sizeOfCompletedGoals.toFloat() / sizeOfGoals.toFloat()

    Box(
        modifier = modifier
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
            Text(
                text = task.title,
                fontSize = 25.sp,
                color = Color(0xFF242636),
                maxLines = 2, // ou 2 se quiser permitir mais linhas
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "${task.timeToComplete} Hours Needed",
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                color = Color((0xFF242636)))

            Row(Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(0.70f),
                    color = Color(0xFF242636),
                    trackColor = Color(0xFF90C323),
                )
                Text("$progress%",
                    color = Color(0xFF242636))
            }
        }
    }
}

@Composable
fun DrawerContent(){
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF2C2C3A)
    ) {
        Text(
            "Tasks infos",
            modifier = Modifier.padding(16.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 25.sp,
            color = Color.White)
        HorizontalDivider()
        NavigationDrawerItem(
            label = {
                Text("Late tasks",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace)
            },
            selected = false,
            onClick = {
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent,
                unselectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                selectedContainerColor = Color(0xFF2D2D3A),
            )
        )
        NavigationDrawerItem(
            label = {
                Text("Completed tasks",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace)
            },
            selected = false,
            onClick = {
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent,
                unselectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                selectedContainerColor = Color(0xFF2D2D3A)
            )
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DatePick(
    actualDay: String,
    openDatePicker: Boolean,
    date: String,
    onDatePickerChange: (Boolean) -> Unit,
    onDateSelected: (String) -> Unit,
    dateState: DatePickerState
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
        onTaskClick = {}
    )
}

@Composable
@Preview
fun SettingsPartPreview(){
    SettingsPart(
        "",
        date = "",
        onDatePickerChange = {},
        onDateSelected = {},
        onAddTaskClick = {},
        openDatePicker = false
    )
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen(
        onAddTaskClick = { },
        onTaskClick = {},
        viewModel = TODO(),
        onNotificationClick = TODO(),
    )
}

@Composable
@Preview
fun UserHomeScreenPreview(){
    UserHomeScreen(
        uiState = HomeScreenUiState(name = "Yuri Lima")
    )
}

@Preview
@Composable
fun DelayTaskCardPreview(){
    DelayTaskCard(
        task = Task(
            title = "Teste de uma atividade muito grande",
            description = "Uma atividade muito grande"
        )
        , onTaskClick = {})
}

@Composable
@Preview
fun TaskListPreview(){
    TasksList(
        tasks = listOf(
            Task(id = "1",
                title = "Teste de uma atividade muito grande",
                description = "Uma atividade muito grande"),
            Task(id = "2",
                title = "Teste de uma atividade muito grande",
                description = "Uma atividade muito grande"),
            Task(id = "3",
                title = "Lorem",
                description = "Uma atividade muito grande"),
            Task(id = "4",
                title = "Lorem",
                description = "Uma atividade muito grande"),
            Task(id = "5",
                title = "Lorem",
                description = "Uma atividade muito grande"),
            Task(id = "6",
                title = "Lorem",
                description = "Uma atividade muito grande"),
            Task(id = "7",
                title = "Lorem",
                description = "Uma atividade muito grande")
        ),
        onTaskClick = {  }
    )
}