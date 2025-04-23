package com.example.todo

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
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

        composeTestRule.onNodeWithText("Email").performTextInput(TestConfig.testEmail)
        composeTestRule.onNodeWithText("Password").performTextInput(TestConfig.testPassword)

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
        composeTestRule.onNodeWithText("DeadLine").performClick()

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
        composeTestRule.onNodeWithText("DeadLine").performClick()

        composeTestRule.waitUntilAtLeastOneExists(hasText("Select"))
        composeTestRule.onNodeWithText("Select").performClick()

        composeTestRule.onNodeWithTag("time_to_complete").performTextInput("10")

        composeTestRule.onNodeWithTag("add_goals_button").performClick()
        composeTestRule.onNodeWithTag("save_task_button").performClick()


        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("home_screen").fetchSemanticsNodes().isNotEmpty()
        }
    }
}
