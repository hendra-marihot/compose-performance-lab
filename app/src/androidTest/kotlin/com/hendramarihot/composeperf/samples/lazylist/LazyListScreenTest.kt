package com.hendramarihot.composeperf.samples.lazylist

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class LazyListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screenRendersWithoutCrashing() {
        composeTestRule.setContent {
            MaterialTheme {
                LazyListScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("LazyList Keys").assertIsDisplayed()
        composeTestRule.onNodeWithText("Without Keys").assertIsDisplayed()
        composeTestRule.onNodeWithText("With Keys").assertIsDisplayed()
    }

    @Test
    fun tabNavigationSwitchesBetweenDemos() {
        composeTestRule.setContent {
            MaterialTheme {
                LazyListScreen(onBack = {})
            }
        }

        composeTestRule.onNodeWithText("Insert at top (no keys)").assertIsDisplayed()

        composeTestRule.onNodeWithText("With Keys").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Insert at top (with keys)").assertIsDisplayed()
    }

    @Test
    fun insertButtonAddsItemWithoutKeys() {
        composeTestRule.setContent {
            MaterialTheme {
                WithoutKeysDemo()
            }
        }

        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Insert at top (no keys)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("New Item 10").assertIsDisplayed()
    }

    @Test
    fun insertButtonAddsItemWithKeys() {
        composeTestRule.setContent {
            MaterialTheme {
                WithKeysDemo()
            }
        }

        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()

        composeTestRule.onNodeWithText("Insert at top (with keys)").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("New Item 10").assertIsDisplayed()
    }
}
