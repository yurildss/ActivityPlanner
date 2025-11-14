package com.example.todo

import com.example.todo.model.Goals
import com.example.todo.model.Task
import com.example.todo.model.User
import com.example.todo.model.service.AccountService
import com.example.todo.model.service.StorageService
import com.example.todo.screen.home.HomeScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Suppress("IllegalIdentifier")
class HomeScreenViewModelUnitTest {

    private val accountService = mock<AccountService>()
    private val storageService = mock<StorageService>()
    private lateinit var viewModel: HomeScreenViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    val tasks = listOf(
        Task(
            id = "1",
            title = "Study Kotlin",
            description = "Study the basics of Kotlin for the next project",
            completed = false,
            deadLine = System.currentTimeMillis() + 86400000,  // 1 day from now
            priority = 2,
            tags = mutableListOf("Learning", "Kotlin"),
            goals = mutableListOf(
                Goals(
                    title = "Complete Chapter 1",
                    description = "Finish reading Chapter 1 of Kotlin book",
                    completed = false,
                    deadLine = System.currentTimeMillis() + 43200000,  // 12 hours from now
                    timeToComplete = 3600000,  // 1 hour
                    percentComplete = 0F
                )
            ),
            userId = "user123",
            timeToComplete = 7200000,  // 2 hours
            notificationRead = false
        ),
        Task(
            id = "2",
            title = "Buy Groceries",
            description = "Go to the supermarket and buy vegetables, fruits, and dairy products.",
            completed = false,
            deadLine = System.currentTimeMillis() + 172800000,  // 2 days from now
            priority = 1,
            tags = mutableListOf("Errands", "Shopping"),
            goals = mutableListOf(
                Goals(
                    title = "Buy vegetables",
                    description = "Get vegetables like carrots, potatoes, and onions",
                    completed = false,
                    deadLine = System.currentTimeMillis() + 86400000,  // 1 day from now
                    timeToComplete = 3600000,  // 1 hour
                    percentComplete = 0F
                ),
                Goals(
                    title = "Buy dairy",
                    description = "Get milk, cheese, and butter",
                    completed = false,
                    deadLine = System.currentTimeMillis() + 172800000,  // 2 days from now
                    timeToComplete = 1800000,  // 30 minutes
                    percentComplete = 0F
                )
            ),
            userId = "user456",
            timeToComplete = 10800000,  // 3 hours
            notificationRead = false
        ),
        Task(
            id = "3",
            title = "Complete Project Report",
            description = "Finish the project report and submit it to the manager",
            completed = true,
            deadLine = System.currentTimeMillis() + 604800000,  // 1 week from now
            priority = 3,
            tags = mutableListOf("Work", "Project"),
            goals = mutableListOf(
                Goals(
                    title = "Write Introduction",
                    description = "Write the introduction part of the project report",
                    completed = true,
                    deadLine = System.currentTimeMillis() + 432000000,  // 5 days from now
                    timeToComplete = 3600000,  // 1 hour
                    percentComplete = 100F
                ),
                Goals(
                    title = "Review Conclusion",
                    description = "Review and finalize the conclusion of the report",
                    completed = false,
                    deadLine = System.currentTimeMillis() + 604800000,  // 1 week from now
                    timeToComplete = 7200000,  // 2 hours
                    percentComplete = 0F
                )
            ),
            userId = "user789",
            timeToComplete = 14400000,  // 4 hours
            notificationRead = true
        )
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        runTest {
            whenever(accountService.currentUser).thenReturn(flowOf(User("1", "Test Home Screen")))
            whenever(storageService.getTaskByDay(any())).thenReturn(tasks)
            whenever(storageService.getDelayedTasks()).thenReturn(tasks)
        }

        viewModel = HomeScreenViewModel(storageService, accountService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `should update user name after init`() = runTest {
        assertEquals("Test Home Screen", viewModel.uiState.value.name)
    }

    @Test
    fun `should update serach text`(){
        viewModel.onSearchTextChange("test")
        assertEquals("test", viewModel.uiState.value.searchText)
    }

    @Test
    fun `should update date picker state`(){
        viewModel.setOpenDatePicker(true)
        assertEquals(true, viewModel.uiState.value.openDatePicker)
    }

    @Test
    fun `should update actual day and tasks when updateTaskDeadLine is called`() = runTest {
        viewModel.updateTaskDeadLine("28/04/2025")
        assertEquals("28/04/2025", viewModel.uiState.value.actualDay)
        assertEquals(tasks, viewModel.uiState.value.tasksOfTheDay)
    }

    @Test
    fun `init should setUp the user name and delay tasks`() = runTest {
        assertEquals( "Test Home Screen",viewModel.uiState.value.name)
        assertEquals(tasks, viewModel.uiState.value.tasksOfTheDay)
    }

    @Test
    fun `should get delay task`(){
        assertEquals(3, viewModel.uiState.value.notifications)
    }

}