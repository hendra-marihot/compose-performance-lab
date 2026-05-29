package com.hendramarihot.composeperf.samples.recomposition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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

// BAD: List<String> is a read-only interface that could be backed by a mutable list, so
// Compose infers ContactInfo as Unstable. Under Strong Skipping an unstable parameter is
// compared by reference (===), not by equals — so handing this card a brand-new instance
// (even with identical content) fails the check and forces it to recompose.
data class ContactInfo(
    val name: String,
    val phones: List<String>,
)

@Composable
fun UnstableContactCard(info: ContactInfo, modifier: Modifier = Modifier) {
    var recompCount by remember { mutableIntStateOf(0) }
    SideEffect { recompCount++ }

    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Recompositions: $recompCount",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.testTag("unstableRecompCount"),
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
