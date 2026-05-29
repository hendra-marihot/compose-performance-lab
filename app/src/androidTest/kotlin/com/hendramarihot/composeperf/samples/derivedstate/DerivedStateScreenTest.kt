package com.hendramarihot.composeperf.samples.derivedstate

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DerivedStateScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun headerCount(tag: String): Int {
        val text = composeTestRule.onNodeWithTag(tag)
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.Text)
            ?.joinToString("") { it.text }
            .orEmpty()
        return text.filter { it.isDigit() }.toInt()
    }

    private fun scrollListTwice() {
        repeat(2) {
            composeTestRule.onNode(hasScrollAction()).performTouchInput { swipeUp() }
            composeTestRule.waitForIdle()
        }
    }

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

    // Without derivedStateOf the header reads firstVisibleItemIndex directly, so it recomposes
    // on every item-index change while scrolling — the counter climbs well past 1.
    @Test
    fun withoutDerivedStateHeaderRecomposesRepeatedlyWhileScrolling() {
        composeTestRule.setContent {
            MaterialTheme {
                WithoutDerivedState()
            }
        }

        scrollListTwice()

        val count = headerCount("withoutDerivedRecompCount")
        assertTrue("expected many recompositions while scrolling, got $count", count >= 5)
    }

    // With derivedStateOf the header recomposes only when the boolean flips (leaving the top),
    // so the counter stays tiny no matter how far you scroll.
    @Test
    fun withDerivedStateHeaderRecomposesOnlyOnFlip() {
        composeTestRule.setContent {
            MaterialTheme {
                WithDerivedState()
            }
        }

        scrollListTwice()

        val count = headerCount("withDerivedRecompCount")
        assertTrue("expected few recompositions, got $count", count <= 3)
    }
}
