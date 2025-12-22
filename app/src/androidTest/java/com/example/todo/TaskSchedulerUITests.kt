package com.example.todo

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.performTouchInput

//Integration test
@HiltAndroidTest
class TaskSchedulerUITests {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun login() {
        composeTestRule.onNodeWithTag("getStartedButton").performClick()

        composeTestRule.onNodeWithTag("login_screen").assertExists()

        composeTestRule.onNodeWithText("Email").performTextInput("yurildss@hotmail.com")
        composeTestRule.onNodeWithText("Password").performTextInput("88295886yY")

        composeTestRule.onNodeWithTag("login_button").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun register() {
        composeTestRule.onNodeWithTag("getStartedButton").performClick()
        composeTestRule.onNodeWithTag("login_screen").assertExists()
        composeTestRule.onNodeWithText("Sign Up").performClick()
        composeTestRule.onNodeWithTag("register_screen").assertExists()

        composeTestRule.onNodeWithText("Name").performTextInput("Yuri")
        composeTestRule.onNodeWithText("Email").performTextInput(TestConfig.testEmail)
        composeTestRule.onNodeWithText("Password").performTextInput(TestConfig.testPassword)
        composeTestRule.onNodeWithText("Repeat password").performTextInput(TestConfig.testPassword)

        composeTestRule.onNodeWithTag("register_button").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun addTaskWithGoals(){
        login()
        composeTestRule.onNodeWithTag("add_task_button").performClick()
        composeTestRule.onNodeWithTag("add_task_screen").assertExists()

        composeTestRule.onNodeWithText("Title").performTextInput("Test")
        composeTestRule.onNodeWithText("Description").performTextInput("Test")
        composeTestRule.onNodeWithTag("DeadLine_task").performClick()

        composeTestRule.waitUntilExactlyOneExists(hasText("Select"))
        composeTestRule.onNodeWithText("Select").performClick()

        composeTestRule.onNodeWithTag("priority_dropdown").performClick()
        composeTestRule.onNodeWithText("Priority 1").performClick()

        // Check if a item was select
        composeTestRule.onNodeWithText("Priority 1").assertExists()

        composeTestRule.onNodeWithTag("select_a_icon").performClick()
        composeTestRule.onNodeWithText("Home").performClick()

        composeTestRule.onNodeWithTag("add_goals_button").performClick()
        composeTestRule.onNodeWithTag("goals_title").performTextInput("Test goals")
        composeTestRule.onNodeWithTag("goals_description").performTextInput("Test goals")
        composeTestRule.onNodeWithTag("DeadLine_goals").performClick()

        composeTestRule.waitUntilAtLeastOneExists(hasText("Select"))
        composeTestRule.onNodeWithText("Select").performClick()

        composeTestRule.onNodeWithTag("time_to_complete").performTextInput("10")

        composeTestRule.onNodeWithTag("confirm_goals_button").performClick()
        composeTestRule.onNodeWithTag("save_task_button").performClick()


        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
    }

    //Navigate to a task by click in a task by the notification screen
    @Test
    fun viewTaskByNotification(){
        login()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("has_notification").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("has_notification").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("notification_screen").fetchSemanticsNodes().isNotEmpty()
        }


        composeTestRule.onNodeWithTag("notification_screen").assertExists()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("task_title", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        val taskTitle = composeTestRule
            .onNodeWithTag("task_title", useUnmergedTree = true)
            .fetchSemanticsNode()
            .config[SemanticsProperties.Text]
            .joinToString("") { it.text }

        composeTestRule.onNodeWithTag("tag_task${taskTitle}")
        composeTestRule.onNodeWithText(taskTitle).assertExists()
    }

    @Test
    fun slideTo100Percent() {
        val sliderNode = composeTestRule
            .onNodeWithTag("slider_goal_progress")
            .assertExists()

        sliderNode.performTouchInput {
            down(centerLeft)
            moveTo(centerRight)
            up() //levantamento do dedo
        }

        composeTestRule
            .onNodeWithText("100%", useUnmergedTree = true)
            .assertExists()
    }

    //Make a task full complete
    @Test
    fun makeTaskComplete(){

        addTaskWithGoals()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("delay_task_card").fetchSemanticsNodes().isNotEmpty()
        }

        // performa o click no primeiro node encontrado
        composeTestRule
            .onAllNodesWithTag("delay_task_card")[0]
            .performClick()


        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("goals_card", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("goals_card_switcher", useUnmergedTree = true).performClick()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("view_goals_card", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }

        slideTo100Percent()

    }

    //Navigate to the completed tasks screen
    @Test
    fun allTasksCompleted(){
        login()

        composeTestRule.onNodeWithTag("tasks_menu").performClick()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("drawer_content", useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("all_completed_task").performClick()
        composeTestRule.onNodeWithTag("completed_task_screen").assertExists()
    }


}
