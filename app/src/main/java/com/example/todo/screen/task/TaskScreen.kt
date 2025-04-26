package com.example.todo.screen.task

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
    viewModel: TaskScreenViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
             ){

    val uiState = viewModel._taskScreenState.value
    val isLoading = viewModel.isLoading

    if(!isLoading.value){
        Box(modifier
            .fillMaxSize().padding(top = 20.dp)
            .background(Color(0xFF1D1D2A))){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                TaskInfos(onBackClick, uiState)
                Spacer(modifier = Modifier.height(15.dp))
                TaskGoalsAndTeams(
                    taskScreenState = uiState
                )
                Spacer(modifier = Modifier.height(15.dp))
                GoalsList(
                    uiState.goals,
                    sliderPositionFunc = viewModel::sliderPositionFun,
                    sliderPosition = viewModel.sliderPosition.floatValue,
                    onUpdatePercentGoalsClick = viewModel::updatePercentGoals,
                    expanded = uiState.goalsCardExpand,
                    onCardGoalsExpand = viewModel::onCardGoalsExpand
                )
            }
        }
    }
}

@Composable
private fun TaskInfos(
    onBackClick: () -> Unit,
    uiState: TaskScreenState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
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
        lineHeight = 48.sp,
        fontFamily = FontFamily.Monospace,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 10.dp)
    )
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
                    if(taskScreenState.isCompleted){
                        Icon(Icons.Default.Check,
                            null,
                            tint = Color(0xFFB4EF2C)
                        )
                    }else{
                        Icon(Icons.Default.Close,
                            null,
                            tint = Color(0xFFB4EF2C)
                        )
                    }
                }
                Text(taskScreenState.isCompletedString,
                    fontSize = 15.sp,
                    color = Color(0xFF242636),
                    modifier = Modifier.padding(start = 10.dp),
                    fontFamily = FontFamily.Monospace
                )
            }

            Row(modifier = Modifier.fillMaxWidth().padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(taskScreenState.description, modifier = Modifier.fillMaxWidth(0.75f),
                    fontFamily = FontFamily.Monospace,)
                Text("${taskScreenState.completedGoals}/${taskScreenState.goals.size}",
                    fontSize = 45.sp,
                    color = Color(0xFF242636),
                    fontFamily = FontFamily.Monospace
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if(taskScreenState.goals.size.toFloat() != 0f){
                LinearProgressIndicator(
                    progress = { (taskScreenState.completedGoals / taskScreenState.goals.size).toFloat() },
                    modifier = Modifier.fillMaxWidth(0.75f),
                    color = Color(0xFF242636),
                    trackColor = Color(0xFF90C323),
                )
                Text("${(taskScreenState.completedGoals / taskScreenState.goals.size)*100}%",
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
fun GoalsList(goals: MutableList<Goals> = mutableListOf(),
              sliderPositionFunc: (Float)-> Unit,
              sliderPosition: Float,
              onUpdatePercentGoalsClick: (Int)-> Unit,
              expanded: Boolean,
              onCardGoalsExpand: (Boolean)->Unit
              ){
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
                    color = Color.White,
                    fontFamily = FontFamily.Monospace)
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
                        GoalsCardSwitcher(
                            goalsItem,
                            sliderPositionFunc = sliderPositionFunc,
                            sliderPosition = sliderPosition,
                            onUpdatePercentGoalsClick = {
                                onUpdatePercentGoalsClick(
                                    goals.indexOf(
                                        goalsItem
                                    )
                                )
                            },
                            expanded = expanded,
                            expandedChange = onCardGoalsExpand
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ViewGoalsCard(
    goal: Goals = Goals(),
    sliderPositionFunc: (Float) -> Unit,
    sliderPosition: Float,
    onUpdatePercentGoalsClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF588D7D))
            .padding(10.dp).testTag("view_goals_card")
    ){

        Column(Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(goal.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp),
                fontFamily = FontFamily.Monospace
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
                Text("${goal.description}.",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(top = 5.dp)
                )
                CircularProgressIndicator(
                    progress = goal.percentComplete,
                    modifier = Modifier
                        .width(64.dp)
                        .padding(10.dp),
                    color = Color(0xFFB7EE35),
                    trackColor = Color(0xFF4E7B6E),
                    strokeWidth = 6.dp
                )
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPositionFunc(it) },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF4E7B6E),
                        activeTrackColor = Color(0xFFB7EE35)
                    ),
                    modifier = Modifier.fillMaxWidth(0.85f).testTag("slider_goal_progress")
                )
                Text(
                    "${(sliderPosition * 100).toInt()}%",
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
            }
            Button(onClick =
                onUpdatePercentGoalsClick
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun GoalsCard(goal: Goals){
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF588D7D))
            .padding(10.dp).testTag("goals_card")
    ){
        Column(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFB4EF2C))
                        .fillMaxWidth(0.75f)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(goal.title, color = Color(0xFF242636))
                }
                Box(modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        progress = goal.percentComplete,
                        modifier = Modifier
                            .fillMaxSize(),
                        color =  Color(0xFFB7EE35),
                        trackColor =Color(0xFF4E7B6E),
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GoalsCardSwitcher(
    goal: Goals = Goals(), sliderPositionFunc: (Float)->Unit,
    sliderPosition: Float,
    onUpdatePercentGoalsClick: ()-> Unit, expanded: Boolean,
    expandedChange: (Boolean) -> Unit) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { expandedChange(!expanded) }
        .padding(8.dp).testTag("goals_card_switcher")
    ) {
        AnimatedContent(targetState = expanded, transitionSpec = {
            fadeIn() with fadeOut()
        }) { isExpanded ->
            if (isExpanded) {
                ViewGoalsCard(
                    goal = goal,
                    sliderPositionFunc = sliderPositionFunc,
                    sliderPosition = sliderPosition,
                    onUpdatePercentGoalsClick = onUpdatePercentGoalsClick
                )
            } else {
                GoalsCard(goal = goal)
            }
        }
    }
}


@Composable
@Preview
fun PreviewGoalsCard(){
    ViewGoalsCard(
        goal = Goals(
            title = "Testando a visualização",
            description = "Aqui eu estou testando a visualização de um Goals de uma terafa para ver se" +
                    "eu paro de enrolar e termino logo isso hihi",
            percentComplete = 0.5F
        ),
        sliderPositionFunc = {},
        sliderPosition = 0.3f,
        onUpdatePercentGoalsClick = {}
    )
}

@Composable
@Preview
fun GoalsCardPreview(){
    Surface(modifier = Modifier.fillMaxWidth()) {
        GoalsCard(
            goal = Goals(
                title = "Teste",
                description = "Testando o card do goals"
            )
        )
    }
}

@Composable
@Preview
fun GoalsPreview(){
    GoalsList(
        goals = mutableListOf(Goals()),
        sliderPositionFunc = {},
        sliderPosition = 0.3F,
        onUpdatePercentGoalsClick = {},
        expanded = false,
        onCardGoalsExpand = {}
    )
}

@Composable
@Preview
fun TaskGoalsAndTeamsPreview(){
    TaskGoalsAndTeams(
        taskScreenState = TaskScreenState( description = "Testando alguma coisa nova que me veio a cabeça",goals = mutableListOf(Goals()))
    )
}

@Composable
@Preview
fun TaskInfosPreview(){
    TaskInfos(onBackClick = {}, uiState = TaskScreenState())
}
@Composable
@Preview
fun TaskInfoPreview(){
    TaskInfo()
}
