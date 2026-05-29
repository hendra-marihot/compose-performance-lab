package com.hendramarihot.composeperf.samples.derivedstate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

// isAtTop is a lambda so the scroll-state read happens INSIDE this composable, which is what
// subscribes it. The "without" demo passes a lambda that reads firstVisibleItemIndex directly
// (so this header recomposes on every item-index change); the "with" demo passes one that reads
// a derivedStateOf boolean (so it recomposes only when the result actually flips). The counter
// therefore reflects the real difference between the two approaches.
@Composable
internal fun StatusHeader(
    isAtTop: () -> Boolean,
    isOptimized: Boolean,
    modifier: Modifier = Modifier,
) {
    var recompCount by remember { mutableIntStateOf(0) }
    SideEffect { recompCount++ }

    val atTop = isAtTop()

    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isOptimized) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            },
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Text(
                text = "Header recompositions: $recompCount",
                style = MaterialTheme.typography.labelMedium,
                color = if (isOptimized) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
                modifier = Modifier.testTag(
                    if (isOptimized) "withDerivedRecompCount" else "withoutDerivedRecompCount",
                ),
            )
            Text(
                text = if (atTop) "At top of list" else "Scrolled down",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isOptimized) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
            )
        }
    }
}
