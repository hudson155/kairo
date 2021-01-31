package kotlin

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class LimberCollectionsTest {
  @Test
  fun `singleNullOrThrow - list`() {
    assertNull(emptyList<Int>().singleNullOrThrow())
    assertEquals(1, listOf(1).singleNullOrThrow())
    assertFails { listOf(1, 2).singleNullOrThrow() }
  }

  @Test
  fun `singleNullOrThrow - sequence iterable`() {
    assertNull(emptySequence<Int>().asIterable().singleNullOrThrow())
    assertEquals(1, sequenceOf(1).asIterable().singleNullOrThrow())
    assertFails { sequenceOf(1, 2).asIterable().singleNullOrThrow() }
  }

  @Test
  fun `isNotEmpty - list`() {
    assertFalse(emptyList<Boolean>().isNotEmpty())
    assertTrue(listOf(true, false).isNotEmpty())
  }

  @Test
  fun `isNotEmpty - sequence iterable`() {
    assertFalse(emptySequence<Boolean>().asIterable().isNotEmpty())
    assertTrue(sequenceOf(true, false).asIterable().isNotEmpty())
  }

  @Test
  fun ifNull() {
    assertFails("called!") { null.ifNull { error("called!") } }
    assertEquals("value", "value".ifNull { error("called!") })
  }
}
