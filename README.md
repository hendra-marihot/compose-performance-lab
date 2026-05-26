# Compose Performance Lab

An interactive Android app that demonstrates the four most common Jetpack Compose
performance mistakes — and their fixes — through side-by-side comparisons with live
recomposition counters. Each sample isolates a single anti-pattern so the counter
difference speaks for itself.

This is a portfolio project. It exists to show that I understand how Compose's
recomposition and skipping model works at a level deeper than "follow the docs."

<!-- TODO: Replace with a GIF of the Recomposition & Stability screen.
     Capture: open the app → tap "Recomposition & Stability" → tap "Trigger parent
     recomposition" 5 times. The unstable counter climbs; the stable counter stays at 1. -->
![Demo](docs/demo.gif)

---

## Why these four patterns

Compose performance issues fall into two categories: things the compiler/runtime
handles automatically, and things it can't. These four sit right on that boundary —
they're where developers most often assume the framework will optimize for them
when it won't.

| Sample | Anti-pattern | Fix | What you see |
|---|---|---|---|
| **Stability** | `data class` holding `List<T>` — compiler infers Unstable | `@Immutable` annotation | Unstable card recomposes every parent trigger; stable card stays at 1 |
| **LazyList Keys** | `items(list.size) { index -> }` — positional identity | `items(list, key = { it.id })` | Insert at top: without keys, all counters reset; with keys, only the new item starts at 1 |
| **derivedStateOf** | Reading `scrollState.firstVisibleItemIndex` directly | `derivedStateOf { ... }` wrapper | Without: header recomposes every scroll frame; with: only on boolean flip |
| **Lambda references** | Inline `{ doSomething(value) }` — new object per recomposition | `remember(value) { { doSomething(value) } }` | Without: all 4 button cards recompose; with: only the one whose selection changed |

Each sample uses a `HorizontalPager` with tabs so you can switch between the bad and
good implementations on the same screen. The format was deliberate — two separate
screens would force the user to remember counter values across navigation. Tabs keep
both versions a swipe apart.

---

## Technical decisions

**Recomposition counters use `SideEffect`, not a custom mechanism.**
`SideEffect` fires after every successful recomposition of its enclosing scope. The
counter is `remember`-ed *inside* the composable being measured, never hoisted to a
parent. Hoisting would cause the parent to observe counter mutations, triggering
additional recompositions that aren't part of the anti-pattern — inflating the count
and breaking the demo's honesty.

**No ViewModel. No dependency injection.**
Every sample's state lives in `remember` and `mutableStateOf`. ViewModels and Hilt
would add layers that have nothing to do with what's being demonstrated — Compose
runtime behavior. The absence is the point: this app has no business logic, no
network calls, no persistence. Adding architecture for its own sake would signal
the opposite of good judgment.

**Macrobenchmark suite included, not as proof of performance, but of practice.**
The `:macrobenchmark` module contains frame-timing benchmarks for the recomposition
and scroll samples, plus a baseline profile generator that walks all four samples.
The benchmarks use `CompilationMode.Partial()` and `StartupMode.WARM` — the
configuration you'd use in a real CI pipeline. I included them because knowing *how*
to measure is as important as knowing *what* to optimize, and setting up macrobenchmarks
correctly (UiAutomator selectors, emulator suppression, benchmark build types) is
non-trivial.

**`runtime-tracing` is a debug-only dependency.**
The app includes `androidx.compose.runtime:runtime-tracing` scoped to `debugImplementation`
so Perfetto system traces capture Compose-level function names. This is the same
setup I'd use to diagnose recomposition issues in a real app — it's here because
it belongs in the workflow, not because it changes runtime behavior.

**Type-safe navigation with `@Serializable` routes.**
Routes are `@Serializable data object` declarations — no string-based routing.
This is the Navigation 2.8+ pattern that catches route typos at compile time
rather than at runtime.

---

## Architecture

```
compose-performance-lab/
├── app/                              # Main application module
│   └── src/main/kotlin/.../
│       ├── MainActivity.kt           # Single Activity, edge-to-edge
│       ├── navigation/
│       │   └── PerfNavHost.kt        # All routes + home screen
│       └── samples/
│           ├── recomposition/        # Stability: @Immutable vs unstable data class
│           ├── lazylist/             # Keys: positional vs identity-based
│           ├── derivedstate/         # derivedStateOf vs raw state read
│           └── lambda/              # remember(key) lambda vs inline allocation
│
└── macrobenchmark/                   # Benchmark module (benchmark build type only)
    └── src/main/kotlin/.../
        ├── BaselineProfileGenerator.kt
        ├── RecompositionBenchmark.kt
        ├── ScrollBenchmark.kt
        └── Constants.kt
```

Each sample follows a consistent pattern: a `Screen.kt` container (Scaffold + HorizontalPager +
TabRow), a `Without*.kt` file (anti-pattern), and a `With*.kt` file (fix). When the
bad and good demos share a data class or composable, it lives in its own file — not
buried inside one consumer.

The app has no layers beyond what you see. `MainActivity` sets up `MaterialTheme` and
mounts `PerfNavHost`. There is no repository layer, no use-case layer, no module
dependency graph to trace. That's not laziness — a demo app with architecture
layers it doesn't need would be a worse portfolio signal than one without them.

---

## Testing and CI

**UI tests** (`androidTest/`): 5 test classes covering navigation to every screen,
tab rendering, and user interactions (button taps, counter increments). Uses
Compose's `createComposeRule()` test harness.

**Unit tests** (`test/`): Data class property and equality tests for `KeyedItem` and
`ContactInfo` — the shared types used across samples.

**Macrobenchmark compilation**: The benchmark module only has a `benchmark` build type
(no debug variant). CI compiles it with `:macrobenchmark:compileBenchmarkKotlin` to
catch breakage without needing a physical device.

**CI pipeline** (GitHub Actions): A single job runs `assembleDebug`, `testDebugUnitTest`,
`lintDebug`, and the benchmark compilation. Lint and test reports are uploaded as
artifacts. The workflow uses concurrency groups to cancel in-progress runs on the
same branch.

---

## Non-goals

These were considered and intentionally excluded:

- **Compose compiler metrics/reports** — The stability report would show the same
  information the counters already demonstrate visually. Adding it would shift the
  app from "interactive demo" to "tooling showcase," which is a different project.
- **Strong skipping mode comparison** — Kotlin 2.0+ changed how the compiler handles
  stability inference. A comparison would be interesting but adds a compiler-version
  dimension that complicates the narrative.
- **Real-world app integration** — These patterns are isolated on purpose. Embedding
  them in a larger app would make it harder to attribute recomposition counts to a
  single cause.
- **Published benchmark numbers** — Frame timing results vary by device, emulator
  config, and thermal state. The infrastructure to measure is included; specific
  numbers would be misleading without a controlled environment.

---

## Running it

```bash
./gradlew :app:installDebug    # Install on a connected device or emulator (API 26+)
```

Full CI check:

```bash
./gradlew assembleDebug testDebugUnitTest lintDebug :macrobenchmark:compileBenchmarkKotlin
```

---

## Related projects

<!-- TODO: Add links to your other portfolio repositories here.
     Example format:
     - [Repo Name](https://github.com/...) — one-line description of what it demonstrates -->

---

## License

```
Copyright 2026 Hendra Marihot

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
