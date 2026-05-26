package com.hendramarihot.composeperf.samples.lazylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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

// BAD: Without keys, LazyColumn uses positional identity. Inserting at index 0 shifts
// all existing items down by one position, causing every item to be considered "new"
// at its slot — the composition is reused but the state (recompCount) resets for each.
@Composable
fun WithoutKeysDemo(modifier: Modifier = Modifier) {
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
            Text("Insert at top (no keys)")
        }

        LazyColumn {
            items(items.size) { index ->
                var recompCount by remember { mutableIntStateOf(0) }
                SideEffect { recompCount++ }

                ListItem(
                    headlineContent = { Text(items[index].label) },
                    trailingContent = {
                        Text(
                            text = "Recomp: $recompCount",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                )
                HorizontalDivider()
            }
        }
    }
}
