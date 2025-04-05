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

@HiltViewModel
class TaskScreenViewModel
@Inject constructor(
    private val storageService: StorageService,
    private val savedStateHandle: SavedStateHandle
) : ToDoAppViewModel(){

    var _taskScreenState = mutableStateOf(TaskScreenState())
        private set

    var _sliderPosition = mutableFloatStateOf(0F)
        private set

    val taskId = savedStateHandle.get<String>("taskId")
    var task = mutableStateOf<Task?>(null)

    init {
        getTask()

    }

    fun getTask(){
        launchCatching {
            if (taskId != null) {
                task.value = storageService.getTask(taskId)
            }
        }
    }

    fun sliderPosition(position: Float){
        _sliderPosition.value = position
    }

    private fun TaskToTaskScreenState(){
        _taskScreenState.value = TaskScreenState(
            title = task.value!!.title,
            description = task.value!!.description,
            deadLine = task.value!!.dateInBrazilianFormat,
            priority = task.value!!.priority.toString(),
            gols = task.value!!.gols,
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

}

//Necessario opter o id da tarefa, que será passado via rota
//Após isso iremos buscar a tarefa no banco de dados e preencher os campos
//Tudo isso ocorrerá no init
//Precisamos de uma variavel que irá controlar a %(percentComplete) de progresso do Goals
//Esse progresso precisará ser salvo no banco

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
){

    val isCompletedString = if (isCompleted) "Finish task" else "Not Completed"
}

data class GoalsScreenState(
    val openGoalsDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val timeToComplete: String = "",
    val percentComplete: Int = 0,
)