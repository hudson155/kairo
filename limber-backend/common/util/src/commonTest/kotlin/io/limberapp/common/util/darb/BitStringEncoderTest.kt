package io.limberapp.common.util.darb

import kotlin.test.Test
import kotlin.test.assertEquals

class BitStringEncoderTest {
  @Test
  fun encode() {
    assertEquals("", BitStringEncoder.encode(emptyList()))
    assertEquals("0", BitStringEncoder.encode(listOf(false)))
    assertEquals("1", BitStringEncoder.encode(listOf(true)))
    assertEquals("0000", BitStringEncoder.encode(listOf(false, false, false, false)))
    assertEquals("0001", BitStringEncoder.encode(listOf(false, false, false, true)))
    assertEquals("1000", BitStringEncoder.encode(listOf(true, false, false, false)))
    assertEquals("1111", BitStringEncoder.encode(listOf(true, true, true, true)))
    assertEquals("00000", BitStringEncoder.encode(listOf(false, false, false, false, false)))
    assertEquals("00001", BitStringEncoder.encode(listOf(false, false, false, false, true)))
    assertEquals("10000", BitStringEncoder.encode(listOf(true, false, false, false, false)))
    assertEquals("11111", BitStringEncoder.encode(listOf(true, true, true, true, true)))
  }

  @Test
  fun decode() {
    assertEquals(emptyList(), BitStringEncoder.decode(""))
    assertEquals(listOf(false), BitStringEncoder.decode("0"))
    assertEquals(listOf(true), BitStringEncoder.decode("1"))
    assertEquals(listOf(false, false, false, false), BitStringEncoder.decode("0000"))
    assertEquals(listOf(false, false, false, true), BitStringEncoder.decode("0001"))
    assertEquals(listOf(true, false, false, false), BitStringEncoder.decode("1000"))
    assertEquals(listOf(true, true, true, true), BitStringEncoder.decode("1111"))
    assertEquals(listOf(false, false, false, false, false), BitStringEncoder.decode("00000"))
    assertEquals(listOf(false, false, false, false, true), BitStringEncoder.decode("00001"))
    assertEquals(listOf(true, false, false, false, false), BitStringEncoder.decode("10000"))
    assertEquals(listOf(true, true, true, true, true), BitStringEncoder.decode("11111"))
  }
}
