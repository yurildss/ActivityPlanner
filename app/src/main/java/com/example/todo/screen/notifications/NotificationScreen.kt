package com.example.todo.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.model.Task
import org.checkerframework.checker.units.qual.C

@Composable
fun NotificationScreen(
    viewModel: NotificationScreenViewModel = hiltViewModel()
) {

    val tasks = viewModel.notificationScreenState.value.taskList

    NotificationsList(tasks)

}

@Composable
private fun NotificationsList(tasks: List<Task>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))
    ) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)) {
            items(tasks) { task ->
                NotificationCardScreen(task = task, modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
fun NotificationCardScreen(modifier: Modifier = Modifier, task: Task){
    Box(
        modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .background(Color(0xFF0E0E15))
    ){
        Column(Modifier.fillMaxWidth().padding(10.dp)) {

            Text(
                text = "The task ${task.title} is late",
                fontFamily = FontFamily.Monospace,
                color = Color.White
            )
            
            Text(
                text = "DeadLine: ${task.deadLine}",
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
        task = TODO()
    )
}

@Composable
@Preview
fun NotificationsListPreview() {
    NotificationsList(
        tasks = listOf(Task(title = "Tesk 1"), Task(title = "Tesk 2"), Task(title = "Tesk 3"))
    )
}

@Composable
@Preview
fun NotificationScreenPreview() {
    NotificationScreen()
}