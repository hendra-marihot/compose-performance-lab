package com.hendramarihot.composeperf.samples.lambda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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

// "INLINE" tab. The lambda `{ selectedIndex = index }` is written inline with no remember.
// Pre-2024 this allocated a new function object on every parent recomposition and forced
// ActionButton to recompose. Under Strong Skipping (default since Kotlin 2.0.20) the compiler
// auto-memoizes this lambda, so ActionButton receives a stable reference and SKIPS — its
// counter stays at 1 when you trigger a parent recomposition, identical to the "Remembered"
// tab. Tap "Trigger parent recomposition" and watch the child counters hold at 1.
@Composable
fun UnrememberedLambdaDemo(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var parentTrigger by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Parent recompositions: $parentTrigger",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(
            onClick = { parentTrigger++ },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Trigger parent recomposition")
        }

        Text(
            text = "Selected: Item ${selectedIndex + 1}",
            style = MaterialTheme.typography.bodyMedium,
        )

        repeat(4) { index ->
            // The compiler memoizes this inline lambda (its captures — index and the state
            // setter — are stable), so ActionButton sees the same onClick across parent
            // recompositions and skips. Selecting an item still recomposes only the two
            // affected buttons, in both tabs equally.
            ActionButton(
                label = "Item ${index + 1}",
                isSelected = selectedIndex == index,
                onClick = { selectedIndex = index },
            )
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var recompCount by remember { mutableIntStateOf(0) }
    SideEffect { recompCount++ }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Recompositions: $recompCount",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("inlineRecompCount"),
            )
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isSelected) "$label (selected)" else label)
            }
        }
    }
}
