package com.example.todo.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todo.model.Goals


@Composable
fun TaskInfo(
    modifier: Modifier = Modifier,
    viewModel: TaskScreenViewModel = hiltViewModel()
             ){

    val uiState = viewModel._taskScreenState.value
    val isLoading = viewModel.isLoading

    if(!isLoading.value){
        Box(modifier
            .fillMaxSize()
            .background(Color(0xFF1D1D2A))){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Row(
                        Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x166EA68E))
                            .padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            null,
                            tint = Color.White
                        )
                    }
                }
                Row(
                    Modifier
                        .size(150.dp, 54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF363440))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        null,
                        tint = Color.White
                    )

                    Text(
                        text = uiState.deadLine,
                        color = Color.White,
                        fontSize = 17.sp
                    )
                }

                Text(
                    text = "${uiState.title}.",
                    fontSize = 40.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 20.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                TaskGoalsAndTeams(
                    taskScreenState = uiState
                )
                Spacer(modifier = Modifier.height(15.dp))
                GoalsList(uiState.gols)
            }
        }
    }
}

@Composable
fun TaskGoalsAndTeams(
    modifier: Modifier = Modifier,
    taskScreenState: TaskScreenState
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFB4EF2C))
            .padding(15.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF242636))
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(Icons.Default.Check,
                        null,
                        tint = Color(0xFFB4EF2C)
                    )
                }
                Text("Finish tasked",
                    fontSize = 15.sp,
                    color = Color(0xFF242636),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            Text("${taskScreenState.completedGoals}/${taskScreenState.gols.size}",
                fontSize = 50.sp,
                color = Color(0xFF242636)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if(taskScreenState.gols.size.toFloat() != 0f){
                LinearProgressIndicator(
                    progress = { (taskScreenState.completedGoals / taskScreenState.gols.size).toFloat() },
                    modifier = Modifier.fillMaxWidth(0.75f),
                    color = Color(0xFF242636),
                    trackColor = Color(0xFF90C323),
                )
                Text("${(taskScreenState.completedGoals / taskScreenState.gols.size)*100}%",
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color(0xFF242636))
            }else{
                    LinearProgressIndicator(
                        progress = { (taskScreenState.completedGoals / 1).toFloat() },
                        modifier = Modifier.fillMaxWidth(0.75f),
                        color = Color(0xFF242636),
                        trackColor = Color(0xFF90C323),
                    )
                    Text("${(taskScreenState.completedGoals / 1)*100}%",
                        modifier = Modifier.padding(start = 10.dp),
                        color = Color(0xFF242636))
                }
            }
        }
    }
}

@Composable
fun GoalsList(goals: MutableList<Goals> = mutableListOf()){
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Color(0xFF498374)
            ),
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text("Goals(${goals.size})",
                    color = Color.White)
                Icon(Icons.Default.Add,
                    null,
                    tint = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                Modifier.fillMaxSize() ,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (goals.isEmpty()) {
                    item{
                        Text(text = "No goals",
                            color = Color.White,
                            minLines = 2,
                            fontSize = 45.sp
                        )
                    }
                }
                else{
                    items(items = goals, key = { goals.indexOf(it) }) { goalsItem ->
                        GoalsCard(goalsItem)
                    }
                }
            }
        }
    }
}

@Composable
fun ViewGoalsCard(
    goal: Goals = Goals()
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF588D7D))
            .padding(10.dp)
    ){
        var sliderPosition by remember { mutableFloatStateOf(0.75f) }
        Column(Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(goal.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text("Nov 12h, 2024",
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${goal.description}.", color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(top = 5.dp)
                )
                CircularProgressIndicator(
                    progress = goal.percentComplete.toFloat(),
                    modifier = Modifier
                        .width(64.dp)
                        .padding(10.dp),
                    color = Color(0xFF4E7B6E),
                    trackColor = Color(0xFFB7EE35),
                    strokeWidth = 6.dp
                )
            }
            Slider(
                value = goal.percentComplete.toFloat(),
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF4E7B6E),
                    activeTrackColor = Color(0xFFB7EE35)
                )
            )
        }
    }
}

@Composable
@Preview
fun PreviewGoalsCard(){
    ViewGoalsCard()
}

@Composable
fun GoalsCard(goal: Goals){
    Box(
        Modifier
            .fillMaxWidth(0.90f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF588D7D))
            .padding(10.dp)
    ){
        Column(Modifier.fillMaxWidth()) {
            Text(goal.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFB4EF2C))
                        .fillMaxWidth(0.75f)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(goal.description, color = Color(0xFF242636))
                }
                Box(modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        progress = goal.percentComplete.toFloat(),
                        modifier = Modifier
                            .fillMaxSize(),
                        color = Color(0xFF4E7B6E),
                        trackColor = Color(0xFFB7EE35),
                        strokeWidth = 6.dp,
                    )
                }
            }

            Text(goal.dateInBrazilianFormat,
                color = Color.White,
                modifier = Modifier.padding(top = 5.dp),
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp)
        }
    }
}

@Composable
@Preview
fun GoalsCardPreview(){
    Surface(modifier = Modifier.fillMaxWidth()) {
        GoalsCard(
            goal = Goals(title = "Teste", description = "Testando o card do goals")
        )
    }
}

@Composable
@Preview
fun GoalsPreview(){
    GoalsList()
}

@Composable
@Preview
fun TaskGoalsAndTeamsPreview(){
    TaskGoalsAndTeams(
        taskScreenState = TaskScreenState()
    )
}

@Composable
@Preview
fun TaskInfoPreview(){
    TaskInfo()
}
