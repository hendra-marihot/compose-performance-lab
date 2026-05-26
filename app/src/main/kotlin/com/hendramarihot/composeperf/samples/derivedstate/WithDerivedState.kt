package com.hendramarihot.composeperf.samples.derivedstate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// GOOD: derivedStateOf wraps the boolean computation. The resulting State<Boolean>
// only emits a new value when the computed result actually changes (true → false or
// false → true). Composables that read `isAtTop` only recompose on those two transitions
// instead of on every scroll frame.
@Composable
fun WithDerivedState(modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()

    // The lambda inside derivedStateOf reads firstVisibleItemIndex, but the State
    // produced by derivedStateOf only notifies observers when the boolean result flips.
    val isAtTop by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex == 0 }
    }

    Column(modifier = modifier.fillMaxSize()) {
        StatusHeader(
            isAtTop = isAtTop,
            isOptimized = true,
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
