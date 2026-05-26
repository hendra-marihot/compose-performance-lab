package com.hendramarihot.composeperf.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScrollBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollDerivedStateWithout() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.text("derivedStateOf")), 5_000)
            device.findObject(By.text("derivedStateOf"))?.click()
            device.waitForIdle()
        },
    ) {
        val list = device.findObject(By.textContains("Scroll item"))
            ?.parent
            ?.parent
            ?: return@measureRepeated
        list.fling(Direction.DOWN)
        device.waitForIdle()
        list.fling(Direction.UP)
        device.waitForIdle()
    }

    @Test
    fun scrollDerivedStateWith() = benchmarkRule.measureRepeated(
        packageName = TARGET_PACKAGE,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.text("derivedStateOf")), 5_000)
            device.findObject(By.text("derivedStateOf"))?.click()
            device.waitForIdle()
            device.findObject(By.text("With derivedStateOf"))?.click()
            device.waitForIdle()
        },
    ) {
        val list = device.findObject(By.textContains("Scroll item"))
            ?.parent
            ?.parent
            ?: return@measureRepeated
        list.fling(Direction.DOWN)
        device.waitForIdle()
        list.fling(Direction.UP)
        device.waitForIdle()
    }
}
