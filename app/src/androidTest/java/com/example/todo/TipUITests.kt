package com.example.todo

import android.util.Log
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe

@HiltAndroidTest
class TipUITests {

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

        // Verifica se o item foi selecionado
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

    @Test
    fun viewTaskByNotification(){
        login()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("has_notification").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("has_notification").performClick()

// Espera a tela ser exibida
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("notification_screen").fetchSemanticsNodes().isNotEmpty()
        }

// Verifica se a tela apareceu
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

        val bounds = sliderNode.fetchSemanticsNode().boundsInRoot

        sliderNode.performTouchInput {
            swipe(
                start = Offset(bounds.left + 5f, bounds.center.y),
                end = Offset(bounds.right - 5f, bounds.center.y),
                durationMillis = 300
            )
        }

        composeTestRule
            .onNodeWithText("100%")
            .assertExists()
    }



    @Test
    fun makeTaskComplete(){

        addTaskWithGoals()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("task_card").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("task_card").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000){
            composeTestRule.onAllNodesWithTag("goals_card").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("goals_card_switcher").performClick()

        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithTag("view_goals_card").fetchSemanticsNodes().isNotEmpty()
        }

        slideTo100Percent()
    }

}
