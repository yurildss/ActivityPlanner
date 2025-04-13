package com.example.todo.screen.task

import android.util.Log
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

    var sliderPosition = mutableFloatStateOf(0F)
        private set

    var isLoading = mutableStateOf(true)
        private set

    val taskId = savedStateHandle.get<String>("taskId")
    var task = mutableStateOf<Task?>(null)

    init {
        launchCatching{
            getTask()
            Log.d("TaskScreenViewModel", "init2: ${task.value}")
            TaskToTaskScreenState()
            unCompletedGoals()
            completedGoals()
            Log.d("TaskScreenViewModel", "init3: ${task.value}")
            isLoading.value = false
        }
    }

    private suspend fun getTask(){
            if (taskId != null) {
                task.value = storageService.getTask(taskId)
                Log.d("TaskScreenViewModel", "init4: ${storageService.getTask(taskId)}")
            }
    }

    fun sliderPositionFun(position: Float){
        sliderPosition.floatValue = position
    }

    private fun TaskToTaskScreenState(){
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

    fun updatePercentGoals(goalIndex: Int, percent: Float){
        launchCatching {
            if (taskId != null) {
                storageService.updateGoalPercent(taskId, goalIndex, percent)
            }
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