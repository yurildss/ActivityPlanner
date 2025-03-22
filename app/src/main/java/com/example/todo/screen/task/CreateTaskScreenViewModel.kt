package com.example.todo.screen.task

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todo.model.Gols
import com.example.todo.screen.ToDoAppViewModel


class CreateTaskScreenViewModel : ToDoAppViewModel() {


    val options = listOf("-"," Priority 1", "Priority 2", "Priority 3")
    val icons = listOf(
        Icons.Default.Add to "",
        Icons.Default.Home to "Home",
        Icons.Default.Star to "Star",
        Icons.Default.Person to "Person",
        Icons.Default.Settings to "Settings",
        Icons.Default.Email to "Email",
        Icons.Default.Info to "Info",
    )

    var expandedIcon = mutableStateOf(false)
        private set

    var expandedPriority = mutableStateOf(false)
        private set

    var CreateTaskUistate = mutableStateOf(CreateTaskScreenState())
        private set

    var CreateGolsUistate = mutableStateOf(CreateGoalsScreenState())
        private set

    var selectedIcon =   mutableStateOf(icons[0])
        private set

    var selectedPriorityOption = mutableStateOf(options[0])
        private set

    fun onExpandedPriorityChange(newValue: Boolean){
        expandedPriority.value = newValue
    }

    fun onSelectedIconChange(newValue: Pair<ImageVector, String>){
        selectedIcon.value = newValue
    }

    fun onExpandedIconChange(newValue: Boolean){
        expandedIcon.value = newValue
    }

    fun onTitleTaskChange(newValue: String){
        CreateTaskUistate.value =
            CreateTaskUistate.value.copy(title = newValue)
    }

    fun setOpenDatePicker(newValue: Boolean){
        CreateTaskUistate.value =
            CreateTaskUistate.value.copy(openDatePicker = newValue)
    }

    fun setOpenGolsDatePicker(newValue: Boolean){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(openGoalsDatePicker = newValue)
    }

    fun updateGolsDeadLine(newValue: String){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(deadLine = newValue)
    }

    fun updateTaskDeadLine(newValue: String){
        CreateTaskUistate.value =
            CreateTaskUistate.value.copy(deadLine = newValue)
    }

    fun onTitleGolsChange(newValue: String){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(title = newValue)
    }

    fun onDescriptionTaskChange(newValue: String){
        CreateTaskUistate.value =
            CreateTaskUistate.value.copy(description = newValue)
    }

    fun onDescriptionGolsChange(newValue: String){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(description = newValue)
    }

    fun onPriorityTaskChange(newValue: String){
        CreateTaskUistate.value =
            CreateTaskUistate.value.copy(priority = newValue)
    }

    fun onAddGolsClick() {
        Log.d("onAddGolsClick", CreateTaskUistate.value.gols.size.toString())

        // Criando uma nova lista para forçar a recomposição
        CreateTaskUistate.value = CreateTaskUistate.value.copy(
            gols = CreateTaskUistate.value.gols.toMutableList().apply {
                add(Gols()) // Adicionando um novo item
            }
        )
    }

    fun onTimeToCompleteGoalsChange(newValue: String){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(timeToComplete = newValue)
    }

    fun onCreateGoals(idGoals: Int){
        Log.d("onCreateGoals", idGoals.toString())
        CreateTaskUistate.value.gols[idGoals] = Gols(
            title = CreateGolsUistate.value.title,
            description = CreateGolsUistate.value.description,
            timeToComplete = CreateGolsUistate.value.timeToComplete.toLong()
        )
    }
}

data class CreateTaskScreenState(
    val openDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val priority: String = "",
    val gols: MutableList<Gols> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    val timeToComplete: Long  = 0
)

data class CreateGoalsScreenState(
    val openGoalsDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val timeToComplete: String = ""
)

