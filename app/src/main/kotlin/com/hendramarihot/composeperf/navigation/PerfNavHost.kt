package com.hendramarihot.composeperf.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hendramarihot.composeperf.samples.derivedstate.DerivedStateScreen
import com.hendramarihot.composeperf.samples.lambda.LambdaScreen
import com.hendramarihot.composeperf.samples.lazylist.LazyListScreen
import com.hendramarihot.composeperf.samples.recomposition.RecompositionScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data object RecompositionRoute

@Serializable
data object LazyListRoute

@Serializable
data object DerivedStateRoute

@Serializable
data object LambdaRoute

private data class SampleEntry(
    val title: String,
    val description: String,
    val antiPattern: String,
    val fix: String,
    val route: Any,
)

private val samples = listOf(
    SampleEntry(
        title = "Recomposition & Stability",
        description = "Unstable data classes cause unnecessary recompositions when parent recomposes.",
        antiPattern = "data class with List<T> parameter",
        fix = "@Immutable annotation on the data class",
        route = RecompositionRoute,
    ),
    SampleEntry(
        title = "LazyList Keys",
        description = "Missing keys cause all items to recompose when inserting at the top of a list.",
        antiPattern = "items(list.size) { index -> ... }",
        fix = "items(list, key = { it.id }) { ... }",
        route = LazyListRoute,
    ),
    SampleEntry(
        title = "derivedStateOf",
        description = "Reading scroll state directly inside a composable triggers recomposition on every frame.",
        antiPattern = "val isAtTop = scrollState.firstVisibleItemIndex == 0",
        fix = "val isAtTop by remember { derivedStateOf { ... } }",
        route = DerivedStateRoute,
    ),
    SampleEntry(
        title = "Lambda Allocation",
        description = "Inline lambdas passed to child composables are new instances on each recomposition.",
        antiPattern = "Button(onClick = { doSomething(value) })",
        fix = "val onClick = remember(value) { { doSomething(value) } }",
        route = LambdaRoute,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        composable<HomeRoute> {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Compose Perf Lab") })
                },
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(samples, key = { it.title }) { entry ->
                        SampleCard(
                            entry = entry,
                            onClick = { navController.navigate(entry.route) },
                        )
                    }
                }
            }
        }
        composable<RecompositionRoute> {
            RecompositionScreen(onBack = { navController.popBackStack() })
        }
        composable<LazyListRoute> {
            LazyListScreen(onBack = { navController.popBackStack() })
        }
        composable<DerivedStateRoute> {
            DerivedStateScreen(onBack = { navController.popBackStack() })
        }
        composable<LambdaRoute> {
            LambdaScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
private fun SampleCard(
    entry: SampleEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Bad: ${entry.antiPattern}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
            Text(
                text = "Fix: ${entry.fix}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
