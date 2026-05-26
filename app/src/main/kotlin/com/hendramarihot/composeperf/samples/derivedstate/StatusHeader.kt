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
import androidx.compose.ui.unit.dp

@Composable
internal fun StatusHeader(
    isAtTop: Boolean,
    isOptimized: Boolean,
    modifier: Modifier = Modifier,
) {
    var recompCount by remember { mutableIntStateOf(0) }
    SideEffect { recompCount++ }

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
            )
            Text(
                text = if (isAtTop) "At top of list" else "Scrolled down",
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
