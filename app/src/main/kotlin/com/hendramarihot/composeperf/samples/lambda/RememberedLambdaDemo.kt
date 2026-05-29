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

// "REMEMBERED" tab. remember(index) explicitly caches the lambda. This was the classic fix for
// the inline pattern on the other tab — but under Strong Skipping the compiler already
// memoizes that inline lambda, so this is now redundant for skippability: both tabs behave
// identically and the child counters stay at 1 on a parent recomposition. Explicit remember is
// still the right tool when a lambda captures a value whose identity changes every
// recomposition (which auto-memoization would re-key on).
@Composable
fun RememberedLambdaDemo(modifier: Modifier = Modifier) {
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
            // remember(index) stores the lambda in the composition keyed on `index`, so the
            // same instance is reused across parent recompositions. StableActionButton skips —
            // the same outcome the compiler now gives the inline tab automatically.
            val onClick = remember(index) { { selectedIndex = index } }
            StableActionButton(
                label = "Item ${index + 1}",
                isSelected = selectedIndex == index,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun StableActionButton(
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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("rememberedRecompCount"),
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
