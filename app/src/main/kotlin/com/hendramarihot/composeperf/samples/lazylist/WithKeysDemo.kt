package com.hendramarihot.composeperf.samples.lazylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// GOOD: Providing a stable, unique key ties composition state to the item's identity
// rather than its position. When an item moves slots (because a new item was prepended),
// Compose relocates the existing composition node instead of recreating it.
// Only the genuinely new item starts with a fresh recomposition count of 1.
@Composable
fun WithKeysDemo(modifier: Modifier = Modifier) {
    var items by remember {
        mutableStateOf(List(10) { KeyedItem(id = it, label = "Item ${it + 1}") })
    }

    Column(modifier = modifier) {
        Button(
            onClick = {
                val newId = items.maxOf { it.id } + 1
                items = listOf(KeyedItem(id = newId, label = "New Item $newId")) + items
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text("Insert at top (with keys)")
        }

        LazyColumn {
            items(items = items, key = { it.id }) { item ->
                var recompCount by remember { mutableIntStateOf(0) }
                SideEffect { recompCount++ }

                ListItem(
                    headlineContent = { Text(item.label) },
                    trailingContent = {
                        Text(
                            text = "Recomp: $recompCount",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
                HorizontalDivider()
            }
        }
    }
}
