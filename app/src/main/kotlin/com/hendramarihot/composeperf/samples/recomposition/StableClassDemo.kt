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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

// GOOD: @Immutable is the developer's promise that all properties are deeply immutable
// after construction. The compiler takes it at its word and treats the type as stable, so
// under Strong Skipping it compares instances structurally (equals) and skips recomposition
// when the content is unchanged — even across brand-new instances. (It is a promise, not a
// guarantee: the List could technically be mutated; for real code prefer an ImmutableList.)
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
                modifier = Modifier.testTag("stableRecompCount"),
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
