package com.hendramarihot.composeperf.samples.recomposition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecompositionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Incrementing this forces the scope that reads it to recompose. The "Parent
    // recompositions" Text below reads it inside the Column, so the cards recompose too.
    var parentTrigger by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomposition & Stability") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Each tap hands both cards a brand-new instance with identical content. " +
                    "Under Strong Skipping the unstable class is compared by reference (===), " +
                    "so it recomposes; the @Immutable class is compared structurally, so it skips.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Button(
                onClick = { parentTrigger++ },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Trigger parent recomposition")
            }

            // Reading parentTrigger here subscribes the Column scope, so the cards below are
            // re-invoked on every tap. Building fresh instances each time is intentional: the
            // unstable type fails the referential check and recomposes, the @Immutable type
            // has equal content and is skipped.
            Text(
                text = "Parent recompositions: $parentTrigger",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            val unstableInfo = ContactInfo(
                name = "Alice Smith",
                phones = listOf("+1 555-0100", "+1 555-0101"),
            )
            val stableInfo = StableContactInfo(
                name = "Alice Smith",
                phones = listOf("+1 555-0100", "+1 555-0101"),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Unstable",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                    UnstableContactCard(
                        info = unstableInfo,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Stable",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    StableContactCard(
                        info = stableInfo,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
