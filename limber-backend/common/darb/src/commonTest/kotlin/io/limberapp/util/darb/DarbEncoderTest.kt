package io.limberapp.util.darb

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

/**
 * Changes to this file should be kept in sync with the library's README.
 */
internal class DarbEncoderTest {
  @Test
  fun encode() {
    assertEquals("0.", DarbEncoder.encode(emptyList()))
    assertEquals("1.0", DarbEncoder.encode(listOf(false)))
    assertEquals("1.8", DarbEncoder.encode(listOf(true)))
    assertEquals("4.0", DarbEncoder.encode(listOf(false, false, false, false)))
    assertEquals("4.1", DarbEncoder.encode(listOf(false, false, false, true)))
    assertEquals("4.8", DarbEncoder.encode(listOf(true, false, false, false)))
    assertEquals("4.F", DarbEncoder.encode(listOf(true, true, true, true)))
    assertEquals("5.00", DarbEncoder.encode(listOf(false, false, false, false, false)))
    assertEquals("5.08", DarbEncoder.encode(listOf(false, false, false, false, true)))
    assertEquals("5.80", DarbEncoder.encode(listOf(true, false, false, false, false)))
    assertEquals("5.F8", DarbEncoder.encode(listOf(true, true, true, true, true)))
  }

  @Test
  fun decode() {
    assertEquals(emptyList(), DarbEncoder.decode("0."))
    assertEquals(listOf(false), DarbEncoder.decode("1.0"))
    assertEquals(listOf(true), DarbEncoder.decode("1.8"))
    assertEquals(listOf(false, false, false, false), DarbEncoder.decode("4.0"))
    assertEquals(listOf(false, false, false, true), DarbEncoder.decode("4.1"))
    assertEquals(listOf(true, false, false, false), DarbEncoder.decode("4.8"))
    assertEquals(listOf(true, true, true, true), DarbEncoder.decode("4.F"))
    assertEquals(listOf(true, true, true, true), DarbEncoder.decode("4.f"))
    assertEquals(listOf(false, false, false, false, false), DarbEncoder.decode("5.00"))
    assertEquals(listOf(false, false, false, false, true), DarbEncoder.decode("5.08"))
    assertEquals(listOf(true, false, false, false, false), DarbEncoder.decode("5.80"))
    assertEquals(listOf(true, true, true, true, true), DarbEncoder.decode("5.F8"))
    assertEquals(listOf(true, true, true, true, true), DarbEncoder.decode("5.f8"))
    assertFails { DarbEncoder.decode("") }
    assertFails { DarbEncoder.decode("5.G8") }
  }

  @Test
  fun getComponentsOrNull() {
    assertEquals(Pair(0, ""), DarbEncoder.getComponentsOrNull("0."))
    assertEquals(Pair(1, "0"), DarbEncoder.getComponentsOrNull("1.0"))
    assertEquals(Pair(1, "8"), DarbEncoder.getComponentsOrNull("1.8"))
    assertEquals(Pair(4, "0"), DarbEncoder.getComponentsOrNull("4.0"))
    assertEquals(Pair(4, "1"), DarbEncoder.getComponentsOrNull("4.1"))
    assertEquals(Pair(4, "8"), DarbEncoder.getComponentsOrNull("4.8"))
    assertEquals(Pair(4, "F"), DarbEncoder.getComponentsOrNull("4.F"))
    assertEquals(Pair(4, "f"), DarbEncoder.getComponentsOrNull("4.f"))
    assertEquals(Pair(5, "00"), DarbEncoder.getComponentsOrNull("5.00"))
    assertEquals(Pair(5, "08"), DarbEncoder.getComponentsOrNull("5.08"))
    assertEquals(Pair(5, "80"), DarbEncoder.getComponentsOrNull("5.80"))
    assertEquals(Pair(5, "F8"), DarbEncoder.getComponentsOrNull("5.F8"))
    assertEquals(Pair(5, "f8"), DarbEncoder.getComponentsOrNull("5.f8"))
    assertNull(DarbEncoder.getComponentsOrNull(""))
    assertNull(DarbEncoder.getComponentsOrNull("5.G8"))
  }
}
