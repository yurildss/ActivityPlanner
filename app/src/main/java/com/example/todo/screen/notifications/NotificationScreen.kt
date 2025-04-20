package com.example.todo.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.model.Task
import org.checkerframework.checker.units.qual.C

@Composable
fun NotificationScreen(
    onTaskClick: (String) -> Unit,
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {

    val tasks = viewModel.notificationScreenState.value.taskList

    NotificationsList(tasks, onTaskClick, viewModel::updateTaskNotification)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationsList(tasks: List<Task>, onTaskClick: (String) -> Unit, onSlideToDeletedNotification: (String, Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))
            .padding(top = 30.dp)
    ) {
        if (tasks.isEmpty()) {
            Text("Any notifications", color = Color.White, fontFamily = FontFamily.Monospace)
        }else{
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(tasks, key = {it.id}) { task ->
                    val swipeState = rememberSwipeToDismissBoxState()
                    LaunchedEffect(swipeState.currentValue) {
                        if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart){
                            onSlideToDeletedNotification(task.id, true)
                        }
                    }
                    SwipeToDismissBox(
                        state = swipeState,
                        backgroundContent = {
                            when(swipeState.dismissDirection){
                                SwipeToDismissBoxValue.StartToEnd -> {}
                                SwipeToDismissBoxValue.EndToStart -> {
                                    Box(Modifier.fillMaxSize().background(Color.Red)){
                                        Text("Dismissed",
                                            Modifier.padding(16.dp)
                                                .align(Alignment.CenterEnd),
                                            fontSize = 16.sp,
                                            color = Color.White,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                SwipeToDismissBoxValue.Settled -> {}
                            }
                        },
                        enableDismissFromStartToEnd = false
                    ){
                        NotificationCardScreen(
                            task = task,
                            modifier = Modifier.padding(vertical = 5.dp),
                            onTaskClick = onTaskClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCardScreen(modifier: Modifier = Modifier, task: Task, onTaskClick: (String) -> Unit){
    Box(
        modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(Color(0xFF0E0E15))
            .clickable {
                onTaskClick(task.id)
            }
    ){
        Column(Modifier
            .fillMaxWidth()
            .padding(10.dp)) {

            Row(){
                Text(
                    text = "The task ",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Text(
                    text = task.title,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                )
                Text(
                    text = " is lates",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }
            
            Text(
                text = "DeadLine: ${task.dateInBrazilianFormat}",
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 5.dp)
            )

        }
    }
}

@Composable
@Preview
fun NotificationCardScreenPreview() {
    NotificationCardScreen(
        modifier = TODO(),
        task = TODO(),
        onTaskClick = TODO()
    )
}

@Composable
@Preview
fun NotificationsListPreview() {
    NotificationsList(
        tasks = listOf(Task(title = "Tesk 1"), Task(title = "Tesk 2"), Task(title = "Tesk 3")),
        onTaskClick = { },
        onSlideToDeletedNotification = { _, _ -> }
    )
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationScreen(
        onTaskClick = TODO(),
        viewModel = TODO()
    )
}