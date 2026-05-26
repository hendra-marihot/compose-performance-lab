package com.hendramarihot.composeperf.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() = baselineProfileRule.collect(
        packageName = TARGET_PACKAGE,
    ) {
        pressHome()
        startActivityAndWait()

        device.wait(Until.hasObject(By.text("Recomposition & Stability")), 5_000)

        device.findObject(By.text("Recomposition & Stability"))?.click()
        device.waitForIdle()
        val triggerButton = device.findObject(By.textContains("Trigger parent recomposition"))
        repeat(3) {
            triggerButton?.click()
            device.waitForIdle()
        }
        device.pressBack()
        device.waitForIdle()

        device.findObject(By.text("LazyList Keys"))?.click()
        device.waitForIdle()
        device.findObject(By.text("With Keys"))?.click()
        device.waitForIdle()
        device.findObject(By.textContains("Insert at top"))?.click()
        device.waitForIdle()
        device.pressBack()
        device.waitForIdle()

        device.findObject(By.text("derivedStateOf"))?.click()
        device.waitForIdle()
        val scrollable = device.findObject(By.textContains("Scroll item"))
            ?.parent
            ?.parent
        scrollable?.fling(Direction.DOWN)
        device.waitForIdle()
        scrollable?.fling(Direction.UP)
        device.waitForIdle()
        device.findObject(By.text("With derivedStateOf"))?.click()
        device.waitForIdle()
        device.pressBack()
        device.waitForIdle()

        device.findObject(By.text("Lambda Allocation"))?.click()
        device.waitForIdle()
        device.findObject(By.text("Remembered"))?.click()
        device.waitForIdle()
        device.pressBack()
        device.waitForIdle()
    }
}
