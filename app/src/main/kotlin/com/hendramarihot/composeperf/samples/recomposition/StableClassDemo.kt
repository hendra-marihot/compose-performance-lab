package com.hendramarihot.composeperf.samples.recomposition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// GOOD: @Immutable tells the Compose compiler that all public properties are deeply
// immutable after construction. This allows Compose to skip recomposition when the
// caller recomposes but passes the same instance.
@Immutable
data class StableContactInfo(
    val name: String,
    val phones: List<String>,
)

@Composable
fun StableContactCard(info: StableContactInfo, modifier: Modifier = Modifier) {
    var recompCount by remember { mutableIntStateOf(0) }
    SideEffect { recompCount++ }

    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recompositions: $recompCount",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = info.name,
                style = MaterialTheme.typography.titleMedium,
            )
            info.phones.forEach { phone ->
                Text(text = phone, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
