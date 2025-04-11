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
import com.example.todo.R
import com.example.todo.common.SnackbarManager
import com.example.todo.model.Goals
import com.example.todo.model.Task
import com.example.todo.model.service.StorageService
import com.example.todo.screen.ToDoAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import javax.inject.Inject

@HiltViewModel
class CreateTaskScreenViewModel
@Inject constructor(
    private val storageService: StorageService
) : ToDoAppViewModel() {


    val options = listOf("-","Priority 1", "Priority 2", "Priority 3")
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
        CreateTaskUistate.value = CreateTaskUistate.value.copy(
            tags = CreateTaskUistate.value.tags.toMutableList().apply {
                if (CreateTaskUistate.value.tags.contains(newValue.second)){

                }else if(CreateTaskUistate.value.tags.isEmpty()){
                    add(newValue.second)
                }
                else if(isNotEmpty()){
                    set(0, newValue.second)
                }
            }
        )

        Log.d("TAG", "onSelectedIconChange: ${CreateTaskUistate.value.tags}")
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

        selectedPriorityOption.value = newValue

        Log.d("TAG", "onPriorityTaskChange: $newValue")
    }

    /**
     * Botao para criar um novo Goals no cadastro de uma nova Task
     */
    fun onAddGolsClick() {
        CreateTaskUistate.value = CreateTaskUistate.value.copy(
            gols = CreateTaskUistate.value.gols.toMutableList().apply {
                add(Goals())
            }
        )
    }

    fun onTimeToCompleteGoalsChange(newValue: String){
        CreateGolsUistate.value =
            CreateGolsUistate.value.copy(timeToComplete = newValue)
    }

    fun onRemoveGoalsClick(idGoals: Int){
        CreateTaskUistate.value = CreateTaskUistate.value.copy(
            gols = CreateTaskUistate.value.gols.toMutableList().apply {
                removeAt(idGoals)
            }
        )
    }

    /**
     * botao para criar uma novo Goals
     */
    fun onCreateGoals(idGoals: Int){
        if(isEntryGolsScreenValid()){
            /**
             * Converte a data no formato dd/MM/yyyy para LocalDate do kotlinx.datetime
             */
            val parsedDate = CreateGolsUistate.value.deadLine.split("/").let { parts ->
                LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
            }

            CreateTaskUistate.value.gols[idGoals] = Goals(
                title = CreateGolsUistate.value.title,
                description = CreateGolsUistate.value.description,
                timeToComplete = CreateGolsUistate.value.timeToComplete.toLong(),
                deadLine = parsedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
                isSave = true
            )
            Log.d("TAG", "onCreateGoals: ${CreateTaskUistate.value.gols}")
        }
        SnackbarManager.showMessage(R.string.empty_field_goals)
    }

    fun onGoalsIsSaveChange(){
        CreateGolsUistate.value = CreateGoalsScreenState()
    }

    private fun isEntryGolsScreenValid(): Boolean {
        return CreateGolsUistate.value.title.isNotBlank() &&
                CreateGolsUistate.value.description.isNotBlank() &&
                CreateGolsUistate.value.deadLine.isNotBlank()
    }

    fun onDeleteGoalsClick(idGoals: Int){
        CreateTaskUistate.value = CreateTaskUistate.value.copy(
            gols = CreateTaskUistate.value.gols.toMutableList().apply {
                removeAt(idGoals)
            }
        )
    }

    private fun isEntryTaskValid(): Boolean {

        return CreateTaskUistate.value.title.isNotBlank() &&
                CreateTaskUistate.value.description.isNotBlank() &&
                CreateTaskUistate.value.deadLine.isNotBlank() &&
                CreateTaskUistate.value.priority.isNotBlank() &&
                CreateTaskUistate.value.gols.isNotEmpty()
    }

    fun onSaveTaskClick(onSaveClick: () -> Unit){
        launchCatching {
            if(isEntryTaskValid()){
                storageService.save(CreateTaskUistate.value.toTask())
                onSaveClick()
            }
            SnackbarManager.showMessage(R.string.empty_field_task)
        }

    }
}

fun CreateTaskScreenState.toTask(): Task {

    val parsedDate = deadLine.split("/").let { parts ->
        LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
    }

    var total = 0
    gols.forEach {
        total += it.timeToComplete.toInt()
    }

    val priorityInt = when(priority){
        "Priority 1" -> 1
        "Priority 2" -> 2
        "Priority 3" -> 3
        else -> 0
    }

    return Task(
        title = title,
        description = description,
        deadLine = parsedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds(),
        priority = priorityInt,
        gols = gols,
        tags = tags,
        timeToComplete = total.toLong()
    )
}

data class CreateTaskScreenState(
    val openDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val priority: String = "",
    val gols: MutableList<Goals> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    val timeToComplete: Long  = 0
)

data class CreateGoalsScreenState(
    val openGoalsDatePicker: Boolean = false,
    val title: String = "",
    val description: String = "",
    val deadLine: String = "",
    val timeToComplete: String = "",
)

