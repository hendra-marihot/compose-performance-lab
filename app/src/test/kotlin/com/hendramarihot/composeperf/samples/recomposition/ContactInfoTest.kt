package com.hendramarihot.composeperf.samples.recomposition

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ContactInfoTest {

    @Test
    fun `ContactInfo structural equality works`() {
        val a = ContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val b = ContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        assertEquals(a, b)
    }

    @Test
    fun `ContactInfo with different names are not equal`() {
        val a = ContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val b = ContactInfo(name = "Bob", phones = listOf("+1 555-0100"))
        assertNotEquals(a, b)
    }

    @Test
    fun `ContactInfo with different phones are not equal`() {
        val a = ContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val b = ContactInfo(name = "Alice", phones = listOf("+1 555-0200"))
        assertNotEquals(a, b)
    }

    @Test
    fun `StableContactInfo structural equality works`() {
        val a = StableContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val b = StableContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        assertEquals(a, b)
    }

    @Test
    fun `StableContactInfo with different names are not equal`() {
        val a = StableContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val b = StableContactInfo(name = "Bob", phones = listOf("+1 555-0100"))
        assertNotEquals(a, b)
    }

    @Test
    fun `StableContactInfo copy preserves equality contract`() {
        val original = StableContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val copy = original.copy()
        assertEquals(original, copy)
        assertEquals(original.hashCode(), copy.hashCode())
    }

    @Test
    fun `ContactInfo and StableContactInfo with same data are not equal`() {
        val unstable = ContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        val stable = StableContactInfo(name = "Alice", phones = listOf("+1 555-0100"))
        assertNotEquals(unstable as Any, stable as Any)
    }
}
