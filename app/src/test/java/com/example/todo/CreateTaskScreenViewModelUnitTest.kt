package com.example.todo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.example.todo.model.service.StorageService
import com.example.todo.screen.task.CreateTaskScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("IllegalIdentifier")
class CreateTaskScreenViewModelUnitTest {
    private val storageService = mock<StorageService>()
    private lateinit var viewModel: CreateTaskScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        runTest {
            whenever(storageService.save(any())).thenReturn("#1")
        }

        viewModel = CreateTaskScreenViewModel(storageService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `should add a Goal on the Goals list`(){

        viewModel.onAddGoalsClick()
        assert(viewModel.CreateTaskUistate.value.gols.size == 1)

    }

    @Test
    fun `should remove Goals on Goals list`(){
        viewModel.onAddGoalsClick()
        viewModel.onDeleteGoalsClick(0)
        assert(viewModel.CreateTaskUistate.value.gols.isEmpty())
    }

    @Test
    fun `should create a Goal`(){
        viewModel.onAddGoalsClick()
        viewModel.onTitleGolsChange("Test Goal")
        viewModel.onDescriptionGolsChange("Test Description")
        viewModel.updateGolsDeadLine("01/01/2023")
        viewModel.onTimeToCompleteGoalsChange("1000")
        viewModel.onCreateGoals(0)
        assert(viewModel.CreateTaskUistate.value.gols[0].title == "Test Goal")
        assert(viewModel.CreateTaskUistate.value.gols[0].description == "Test Description")
        assert(viewModel.CreateTaskUistate.value.gols[0].deadLine == 1672531200000)
        assert(viewModel.CreateTaskUistate.value.gols[0].timeToComplete == 1000L)

    }

    @Test
    fun `should create a Task`(){
        var wasCalled = false
        runTest{
            viewModel.onTitleTaskChange("Test Task")
            viewModel.onDescriptionTaskChange("Test Description")
            viewModel.updateTaskDeadLine("01/01/2023")
            viewModel.onPriorityTaskChange("Priority 1")
            viewModel.onSelectedIconChange(Pair(Icons.Default.Check, "Check"))
            `should create a Goal`()
            viewModel.onSaveTaskClick {
                wasCalled = true
            }

            assertTrue(wasCalled)
        }
    }

    @Test
    fun `should not create a Task if title is empty`(){
        var wasCalled = false
        runTest{
            viewModel.onTitleTaskChange("")
            viewModel.onDescriptionTaskChange("Test Description")
            viewModel.updateTaskDeadLine("01/01/2023")
            viewModel.onPriorityTaskChange("Priority 1")
            viewModel.onSelectedIconChange(Pair(Icons.Default.Check, "Check"))
            `should create a Goal`()
            viewModel.onSaveTaskClick {
                wasCalled = true
            }

            assertFalse(wasCalled)
        }
    }

    @Test
    fun `should select icon`(){
        viewModel.onSelectedIconChange(Pair(Icons.Default.Check, "Check"))
        assertEquals(viewModel.selectedIcon.value, Pair(Icons.Default.Check, "Check"))
        assertEquals(viewModel.CreateTaskUistate.value.tags[0], "Check")
    }

    @Test
    fun `should change the select icon`(){
        viewModel.onSelectedIconChange(Pair(Icons.Default.Check, "Check"))
        viewModel.onSelectedIconChange(Pair(Icons.Default.Check, "Check2"))
        assertEquals(viewModel.selectedIcon.value, Pair(Icons.Default.Check, "Check2"))
        assertEquals(viewModel.CreateTaskUistate.value.tags[0], "Check2")
    }
}