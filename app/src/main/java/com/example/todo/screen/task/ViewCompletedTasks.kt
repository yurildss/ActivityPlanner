package com.example.todo.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.model.Task
import com.example.todo.screen.home.DelayTaskCard
import com.example.todo.screen.home.TaskCard
import com.example.todo.ui.theme.TodoTheme

@Composable
fun ViewCompletedTasks(
    modifier: Modifier = Modifier,
    viewModel: CompletedTasksViewModel = hiltViewModel()
)
{
    Box(modifier
        .fillMaxSize()
        .background(Color(0xFF1D1D2A))
        .testTag("completed_task_screen"),
        contentAlignment = Alignment.BottomEnd
    )
    {
        CompletedTasksList(viewModel.completedTaskUiState.value.tasks, {})
    }

}

@Composable
fun CompletedTasksList(
    tasks: List<Task>,
    onTaskClick: (String) -> Unit
) {
    LazyColumn(
        Modifier
            .padding(top = 60.dp, start = 10.dp, end = 10.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (tasks.isEmpty()) {
            item {
                Text(
                    text = "Any completed task has been found",
                    color = Color.White,
                    fontSize = 30.sp,
                )
            }
        } else {
            items(tasks.chunked(2)) { rowTasks ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (task in rowTasks) {
                        if(task.completed){
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
@Preview
fun CompletedTasksListPreview(){
    TodoTheme {
        CompletedTasksList(
            listOf(),
            {}
        )
    }
}