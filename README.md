# Compose Performance Lab

[![CI](https://github.com/hendra-marihot/compose-performance-lab/actions/workflows/ci.yml/badge.svg)](https://github.com/hendra-marihot/compose-performance-lab/actions/workflows/ci.yml)
[![License: Apache-2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)

An Android app that shows the four most common Jetpack Compose performance mistakes next to their fixes. Each screen has a bad tab and a good tab with a live recomposition counter. You interact, and the counters show the difference.

![Recomposition counters: the unstable card recomposes on every parent recomposition, the @Immutable card skips](docs/demo.gif)

## The four patterns

Compose fixes some things for you and not others. These four sit on that line. They are where developers most often assume the framework will optimize and it does not.

| Sample | Anti-pattern | Fix | What you see |
|---|---|---|---|
| **Stability** | `data class` holding `List<T>`, inferred Unstable | `@Immutable` | Unstable card recomposes on every parent trigger. Stable card stays at 1 |
| **LazyList keys** | `items(list.size) { index -> }`, positional identity | `items(list, key = { it.id })` | Insert at top: without keys every counter resets, with keys only the new item starts at 1 |
| **derivedStateOf** | Reading `scrollState.firstVisibleItemIndex` directly | `derivedStateOf { ... }` | Without: header recomposes every scroll frame. With: only when the boolean flips |
| **Lambda references** | Inline `{ doSomething(value) }`, new object per recomposition | `remember(value) { { doSomething(value) } }` | Without: all 4 button cards recompose. With: only the one whose selection changed |

Each screen uses a `HorizontalPager` with tabs, so the bad and good versions are one swipe apart. Separate screens would make you remember counter values across navigation.

## Technical decisions

**Counters use `SideEffect`.** `SideEffect` runs after every successful recomposition of its scope. The counter is `remember`-ed inside the composable being measured, never in a parent. Hoisting it would make the parent observe the counter and recompose too, which would inflate the count.

**No ViewModel, no DI.** State lives in `remember` and `mutableStateOf`. There is no business logic, network or persistence here, so a ViewModel or Hilt would add nothing to the demonstration.

**A Macrobenchmark module.** `:macrobenchmark` has frame-timing benchmarks for the recomposition and scroll samples, plus a baseline profile generator that walks all four screens. The benchmarks use `CompilationMode.Partial()` and `StartupMode.WARM`, the setup you would use in CI. Knowing how to measure matters as much as knowing what to fix.

**`runtime-tracing` in debug builds.** `androidx.compose.runtime:runtime-tracing` is a `debugImplementation` dependency, so Perfetto traces show Compose function names. It is the same setup I use to diagnose recomposition in real apps.

**Type-safe navigation.** Routes are `@Serializable data object` declarations. No strings, so route typos fail at compile time.

## Structure

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
│           └── lambda/               # remember(key) lambda vs inline allocation
│
└── macrobenchmark/                   # Benchmark module (benchmark build type only)
    └── src/main/kotlin/.../
        ├── BaselineProfileGenerator.kt
        ├── RecompositionBenchmark.kt
        ├── ScrollBenchmark.kt
        └── Constants.kt
```

Each sample has a `Screen.kt` container with the pager and tabs, a `Without*.kt` file for the anti-pattern, and a `With*.kt` file for the fix. Shared data classes and composables live in their own files.

There are no layers beyond this. `MainActivity` sets up `MaterialTheme` and mounts `PerfNavHost`. A demo app with layers it does not need would be a worse signal than one without them.

## Tests and CI

- **UI tests** (`androidTest/`): 5 classes covering navigation to every screen, tab rendering, taps and counter increments. They use `createComposeRule()`.
- **Unit tests** (`test/`): property and equality tests for the shared `KeyedItem` and `ContactInfo` classes.
- **Benchmark compilation:** the benchmark module only has a `benchmark` build type. CI compiles it with `:macrobenchmark:compileBenchmarkKotlin` to catch breakage without a device.

CI runs `assembleDebug`, `testDebugUnitTest`, `lintDebug` and the benchmark compilation in one job. Lint and test reports are uploaded as artifacts. Concurrency groups cancel stale runs on the same branch.

## Out of scope

- **Compose compiler reports.** The stability report shows what the counters already show. Adding it would turn an interactive demo into a tooling showcase.
- **Strong skipping comparison.** Kotlin 2.0 changed stability inference. Comparing compiler versions would complicate the story.
- **Embedding in a real app.** The patterns are isolated so each count has one cause.
- **Published benchmark numbers.** Frame times depend on device, emulator config and thermal state. The repo ships the tooling, not numbers from an uncontrolled environment.

## Run it

```bash
./gradlew :app:installDebug    # device or emulator, API 26+
```

Full CI check:

```bash
./gradlew assembleDebug testDebugUnitTest lintDebug :macrobenchmark:compileBenchmarkKotlin
```

Needs JDK 17. Builds with Gradle 9.7, AGP 9.3 and Kotlin 2.4. Targets Android API 26 to 36 and compiles against API 37.

## Related repositories

Each repo in this set covers one area of mobile engineering.

| Repository | Focus |
|---|---|
| [compose-newsreader](https://github.com/hendra-marihot/compose-newsreader) | Multi-module Android, Hilt, Room, offline-first |
| [android-design-system](https://github.com/hendra-marihot/android-design-system) | Compose component library, design tokens |
| **compose-performance-lab** (this repo) | Compose performance anti-patterns and fixes |
| [kmp-currency-converter](https://github.com/hendra-marihot/kmp-currency-converter) | Kotlin Multiplatform, shared business logic |
| [flutter-expense-tracker](https://github.com/hendra-marihot/flutter-expense-tracker) | Clean Architecture, Riverpod, Drift, Material 3 |
| [flutter-ui-toolkit](https://github.com/hendra-marihot/flutter-ui-toolkit) | Reusable widgets, shimmer, adaptive layouts |
| [flutter-platform-bridge](https://github.com/hendra-marihot/flutter-platform-bridge) | Platform channels, native interop |
| [mobile-ci-cd-templates](https://github.com/hendra-marihot/mobile-ci-cd-templates) | CI/CD, release automation |

## License

Copyright 2026 Hendra Marihot. Licensed under the [Apache License 2.0](LICENSE).
