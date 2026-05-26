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

// BAD: scrollState.firstVisibleItemIndex is read directly inside the composable scope.
// Every time the scroll position changes by even one pixel, LazyColumn recomposes the
// scroll state, which forces this composable to reread firstVisibleItemIndex and recompose —
// even when the boolean value of "isAtTop" has not changed.
@Composable
fun WithoutDerivedState(modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()

    // Reading firstVisibleItemIndex here subscribes this entire composable to every
    // scroll frame — not just to changes in the boolean result.
    val isAtTop = scrollState.firstVisibleItemIndex == 0

    Column(modifier = modifier.fillMaxSize()) {
        StatusHeader(
            isAtTop = isAtTop,
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
