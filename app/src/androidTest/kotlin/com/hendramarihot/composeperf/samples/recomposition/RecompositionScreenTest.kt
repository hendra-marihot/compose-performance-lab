package com.hendramarihot.composeperf.samples.recomposition

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecompositionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun recompCount(tag: String): Int {
        val text = composeTestRule.onNodeWithTag(tag)
            .fetchSemanticsNode()
            .config
            .getOrNull(SemanticsProperties.Text)
            ?.joinToString("") { it.text }
            .orEmpty()
        return text.filter { it.isDigit() }.toInt()
    }

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

    // The educational contract: each parent recomposition recomposes the unstable card exactly
    // once (a new instance fails Strong Skipping's referential check) while the @Immutable card
    // never recomposes (compared structurally, equal content). Asserting deltas keeps the test
    // robust against any initial recompositions during layout settling.
    @Test
    fun unstableCardRecomposesOnEachParentTrigger_whileStableCardSkips() {
        composeTestRule.setContent {
            MaterialTheme {
                RecompositionScreen(onBack = {})
            }
        }
        composeTestRule.waitForIdle()

        val unstableBefore = recompCount("unstableRecompCount")
        val stableBefore = recompCount("stableRecompCount")

        repeat(5) {
            composeTestRule.onNodeWithText("Trigger parent recomposition").performClick()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Parent recompositions: 5").assertIsDisplayed()
        assertEquals(
            "unstable card should recompose once per parent recomposition",
            unstableBefore + 5,
            recompCount("unstableRecompCount"),
        )
        assertEquals(
            "stable @Immutable card should be skipped on every parent recomposition",
            stableBefore,
            recompCount("stableRecompCount"),
        )
    }

    @Test
    fun bothCardsDisplayContactInfo() {
        composeTestRule.setContent {
            MaterialTheme {
                RecompositionScreen(onBack = {})
            }
        }

        composeTestRule.onAllNodesWithText("Alice Smith").assertCountEquals(2)
    }
}
