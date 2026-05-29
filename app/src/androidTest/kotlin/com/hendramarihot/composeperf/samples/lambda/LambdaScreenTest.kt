package com.hendramarihot.composeperf.samples.lambda

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
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

    // Honest contract under Strong Skipping: the compiler auto-memoizes the inline lambda, so a
    // parent recomposition does NOT recompose the children — every counter stays at 1, the same
    // as the explicitly-remembered tab. (Before Strong Skipping the inline children would climb.)
    @Test
    fun parentRecompositionDoesNotRecomposeInlineLambdaChildren() {
        composeTestRule.setContent {
            MaterialTheme {
                UnrememberedLambdaDemo()
            }
        }

        repeat(5) {
            composeTestRule.onNodeWithText("Trigger parent recomposition").performClick()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Parent recompositions: 5").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("inlineRecompCount")
            .assertAll(hasText("Recompositions: 1"))
    }

    @Test
    fun parentRecompositionDoesNotRecomposeRememberedLambdaChildren() {
        composeTestRule.setContent {
            MaterialTheme {
                RememberedLambdaDemo()
            }
        }

        repeat(5) {
            composeTestRule.onNodeWithText("Trigger parent recomposition").performClick()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Parent recompositions: 5").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("rememberedRecompCount")
            .assertAll(hasText("Recompositions: 1"))
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
