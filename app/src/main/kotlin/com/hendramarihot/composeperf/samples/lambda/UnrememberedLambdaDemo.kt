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
import androidx.compose.ui.unit.dp

// BAD: The inline lambda `{ selectedIndex = index }` is a new function object on every
// recomposition of UnrememberedLambdaDemo. ActionButton receives a different lambda
// reference each time its parent recomposes, so Compose cannot skip recomposition of
// ActionButton even though its visible output has not changed.
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
            // Each recomposition of this scope creates a new lambda instance —
            // ActionButton always sees a "new" onClick and recomposes.
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
