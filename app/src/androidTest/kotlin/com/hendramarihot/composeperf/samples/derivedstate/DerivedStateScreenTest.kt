package com.hendramarihot.composeperf.samples.derivedstate

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class DerivedStateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screenRendersWithoutCrashing() {
        composeTestRule.setContent {
            MaterialTheme {
                DerivedStateScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("derivedStateOf").assertIsDisplayed()
        composeTestRule.onNodeWithText("Without derivedStateOf").assertIsDisplayed()
        composeTestRule.onNodeWithText("With derivedStateOf").assertIsDisplayed()
    }

    @Test
    fun tabNavigationSwitchesBetweenDemos() {
        composeTestRule.setContent {
            MaterialTheme {
                DerivedStateScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("At top of list").assertIsDisplayed()

        composeTestRule.onNodeWithText("With derivedStateOf").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("At top of list").assertIsDisplayed()
    }

    @Test
    fun withoutDerivedStateShowsScrollItems() {
        composeTestRule.setContent {
            MaterialTheme {
                WithoutDerivedState()
            }
        }

        composeTestRule.onNodeWithText("Scroll item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("At top of list").assertIsDisplayed()
    }

    @Test
    fun withDerivedStateShowsScrollItems() {
        composeTestRule.setContent {
            MaterialTheme {
                WithDerivedState()
            }
        }

        composeTestRule.onNodeWithText("Scroll item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("At top of list").assertIsDisplayed()
    }
}
