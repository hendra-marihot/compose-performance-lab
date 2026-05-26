package com.hendramarihot.composeperf.samples.lambda

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class LambdaScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screenRendersWithoutCrashing() {
        composeTestRule.setContent {
            MaterialTheme {
                LambdaScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Lambda Allocation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unremembered").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remembered").assertIsDisplayed()
    }

    @Test
    fun tabNavigationSwitchesBetweenDemos() {
        composeTestRule.setContent {
            MaterialTheme {
                LambdaScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Trigger parent recomposition").assertIsDisplayed()

        composeTestRule.onNodeWithText("Remembered").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Trigger parent recomposition").assertIsDisplayed()
    }

    @Test
    fun unrememberedDemoTriggerButtonWorks() {
        composeTestRule.setContent {
            MaterialTheme {
                UnrememberedLambdaDemo()
            }
        }

        composeTestRule.onNodeWithText("Parent recompositions: 0").assertIsDisplayed()

        composeTestRule.onNodeWithText("Trigger parent recomposition").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Parent recompositions: 1").assertIsDisplayed()
    }

    @Test
    fun unrememberedDemoItemSelectionWorks() {
        composeTestRule.setContent {
            MaterialTheme {
                UnrememberedLambdaDemo()
            }
        }

        composeTestRule.onNodeWithText("Selected: Item 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 3").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Selected: Item 3").assertIsDisplayed()
    }

    @Test
    fun rememberedDemoItemSelectionWorks() {
        composeTestRule.setContent {
            MaterialTheme {
                RememberedLambdaDemo()
            }
        }

        composeTestRule.onNodeWithText("Selected: Item 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Item 2").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Selected: Item 2").assertIsDisplayed()
    }
}
