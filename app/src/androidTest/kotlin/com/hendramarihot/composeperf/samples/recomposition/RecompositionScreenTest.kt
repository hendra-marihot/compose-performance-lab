package com.hendramarihot.composeperf.samples.recomposition

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class RecompositionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screenRendersWithoutCrashing() {
        composeTestRule.setContent {
            MaterialTheme {
                RecompositionScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Recomposition & Stability").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unstable").assertIsDisplayed()
        composeTestRule.onNodeWithText("Stable").assertIsDisplayed()
    }

    @Test
    fun triggerButtonIncrementsCounter() {
        composeTestRule.setContent {
            MaterialTheme {
                RecompositionScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Trigger parent recomposition (count: 0)")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText("Trigger parent recomposition (count: 1)")
            .assertIsDisplayed()
    }

    @Test
    fun contactInfoIsDisplayed() {
        composeTestRule.setContent {
            MaterialTheme {
                RecompositionScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Alice Smith").assertIsDisplayed()
    }
}
