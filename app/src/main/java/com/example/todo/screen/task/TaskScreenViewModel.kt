package com.example.todo.screen.task

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.todo.model.Goals
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Precisa melhorar os estilos das fontes, estao feias
 * precisa resolver o espaçamento do titulo quando o titulo é grande demais
 * precisa mostrar a descrição da tarefa
 * precisa mudar a paleta de cores principalmente do goals
 * precisa fazer o botão de voltar funcionar
 * precisa fazer o botão de adicionar uma goals em uma tarefa funcionar
 */

@HiltViewModel
class TaskScreenViewModel
@Inject constructor(
    private val storageService: StorageService,
    private val savedStateHandle: SavedStateHandle
) : ToDoAppViewModel(){

    var _taskScreenState = mutableStateOf(TaskScreenState())
        private set

    var sliderPosition = mutableFloatStateOf(0F)


    var isLoading = mutableStateOf(true)
        private set

    val taskId = savedStateHandle.get<String>("taskId")
    private var task = mutableStateOf<Task?>(null)

    init {
        launchCatching{
            getTask()
            taskToTaskScreenState()
            unCompletedGoals()
            completedGoals()
            isLoading.value = false
        }
    }

    private suspend fun updateTaskScreen(){
        getTask()
        taskToTaskScreenState()
        unCompletedGoals()
        completedGoals()
        isLoading.value = false
    }

    private suspend fun getTask(){
            if (taskId != null) {
                task.value = storageService.getTask(taskId)
            }
    }

    fun sliderPositionFun(position: Float){
        sliderPosition.floatValue = position
    }

    private fun taskToTaskScreenState(){
        _taskScreenState.value = TaskScreenState(
            title = task.value!!.title,
            description = task.value!!.description,
            deadLine = task.value!!.dateInBrazilianFormat,
            priority = task.value!!.priority.toString(),
            gols = task.value!!.goals,
            tags = task.value!!.tags,
            isCompleted = task.value!!.isCompleted
        )
    }

    private fun unCompletedGoals(){
        var unCompleted = 0
        _taskScreenState.value.gols.forEach {
            if(!it.isSave){
                unCompleted++
            }
    }
        _taskScreenState.value = _taskScreenState.value.copy(
            unCompletedGoals = unCompleted
        )
    }

    private fun completedGoals(){
        var completed = 0
        _taskScreenState.value.gols.forEach {
            if(it.isSave){
                completed++
            }

    }
        _taskScreenState.value = _taskScreenState.value.copy(
            completedGoals = completed
        )
    }

    fun onCardGoalsExpand(expanded: Boolean){
        _taskScreenState.value = _taskScreenState.value.copy(
            goalsCardExpand = expanded
        )
    }

    fun updatePercentGoals(goalIndex: Int){

        launchCatching {
            if (taskId != null) {

                storageService.updateGoalPercent(taskId, goalIndex, sliderPosition.floatValue)

                _taskScreenState.value = _taskScreenState.value.copy(
                    goalsCardExpand = false
                )
            }

            updateTaskScreen()
        }

    }

}

data class TaskScreenState(
    val openDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val priority: String = "",
    val gols: MutableList<Goals> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    val timeToComplete: Long  = 0,
    val unCompletedGoals: Int = 0,
    val completedGoals: Int = 0,
    val isCompleted: Boolean = false,
    val goalsCardExpand: Boolean = false,
){

    val isCompletedString = if (isCompleted) "Finish task" else "Not Completed"

}

data class GoalsScreenState(
    val openGoalsDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val timeToComplete: String = "",
    val percentComplete: Float = 0F,
)