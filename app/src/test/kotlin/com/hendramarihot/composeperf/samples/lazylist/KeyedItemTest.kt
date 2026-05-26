package com.hendramarihot.composeperf.samples.lazylist

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class KeyedItemTest {

    @Test
    fun `items with same id and label are equal`() {
        val a = KeyedItem(id = 1, label = "Item 1")
        val b = KeyedItem(id = 1, label = "Item 1")
        assertEquals(a, b)
    }

    @Test
    fun `items with different ids are not equal`() {
        val a = KeyedItem(id = 1, label = "Item 1")
        val b = KeyedItem(id = 2, label = "Item 1")
        assertNotEquals(a, b)
    }

    @Test
    fun `items with same id but different labels are not equal`() {
        val a = KeyedItem(id = 1, label = "Item 1")
        val b = KeyedItem(id = 1, label = "Item 2")
        assertNotEquals(a, b)
    }

    @Test
    fun `initial list has unique ids`() {
        val items = List(10) { KeyedItem(id = it, label = "Item ${it + 1}") }
        val ids = items.map { it.id }.toSet()
        assertEquals(10, ids.size)
    }

    @Test
    fun `prepending item produces unique id`() {
        val items = List(10) { KeyedItem(id = it, label = "Item ${it + 1}") }
        val newId = items.maxOf { it.id } + 1
        val newItems = listOf(KeyedItem(id = newId, label = "New Item $newId")) + items

        assertEquals(11, newItems.size)
        val ids = newItems.map { it.id }.toSet()
        assertEquals(11, ids.size)
    }

    @Test
    fun `prepending preserves existing item order`() {
        val items = List(5) { KeyedItem(id = it, label = "Item ${it + 1}") }
        val newId = items.maxOf { it.id } + 1
        val newItems = listOf(KeyedItem(id = newId, label = "New")) + items

        assertEquals(KeyedItem(id = newId, label = "New"), newItems[0])
        for (i in items.indices) {
            assertEquals(items[i], newItems[i + 1])
        }
    }
}
