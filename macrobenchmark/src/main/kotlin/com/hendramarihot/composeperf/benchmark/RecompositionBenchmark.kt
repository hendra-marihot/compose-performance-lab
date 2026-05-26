package com.hendramarihot.composeperf.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecompositionBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun triggerRecomposition() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.text("Recomposition & Stability")), 5_000)
            device.findObject(By.text("Recomposition & Stability"))?.click()
            device.waitForIdle()
        },
    ) {
        val button = device.findObject(By.textContains("Trigger parent recomposition"))
            ?: return@measureRepeated
        repeat(10) {
            button.click()
            device.waitForIdle()
        }
    }
}
