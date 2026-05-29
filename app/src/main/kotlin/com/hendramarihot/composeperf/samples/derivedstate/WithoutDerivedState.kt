package com.hendramarihot.composeperf.samples.derivedstate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// BAD: the lambda reads scrollState.firstVisibleItemIndex directly. StatusHeader invokes it
// during its own composition, so StatusHeader subscribes to the index and recomposes every
// time it changes — once per item scrolled past — even though the "isAtTop" boolean only flips
// at the very top of the list.
@Composable
fun WithoutDerivedState(modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()

    Column(modifier = modifier.fillMaxSize()) {
        StatusHeader(
            isAtTop = { scrollState.firstVisibleItemIndex == 0 },
            isOptimized = false,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(50) { index ->
                Text(
                    text = "Scroll item ${index + 1}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }
        }
    }
}
