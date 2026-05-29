package com.hendramarihot.composeperf.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class PerfNavHostTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreenRendersAllSamples() {
        composeTestRule.setContent {
            MaterialTheme {
                PerfNavHost()
            }
        }

        composeTestRule.onNodeWithText("Compose Perf Lab").assertIsDisplayed()
        composeTestRule.onNodeWithText("Recomposition & Stability").assertIsDisplayed()
        composeTestRule.onNodeWithText("LazyList Keys").assertIsDisplayed()
        composeTestRule.onNodeWithText("derivedStateOf").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lambda Allocation").assertIsDisplayed()
    }

    @Test
    fun navigateToRecompositionScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                PerfNavHost()
            }
        }

        composeTestRule.onNodeWithText("Recomposition & Stability").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Trigger parent recomposition")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToLazyListScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                PerfNavHost()
            }
        }

        composeTestRule.onNodeWithText("LazyList Keys").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Without Keys").assertIsDisplayed()
        composeTestRule.onNodeWithText("With Keys").assertIsDisplayed()
    }

    @Test
    fun navigateToDerivedStateScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                PerfNavHost()
            }
        }

        composeTestRule.onNodeWithText("derivedStateOf").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Without derivedStateOf").assertIsDisplayed()
    }

    @Test
    fun navigateToLambdaScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                PerfNavHost()
            }
        }

        composeTestRule.onNodeWithText("Lambda Allocation").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Unremembered").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remembered").assertIsDisplayed()
    }
}
