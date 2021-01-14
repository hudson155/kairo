package io.limberapp.common.util.string

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class StringTest {
  @Test
  fun joinNames() {
    assertNull(fullName(emptyList()))
    assertEquals("Jeff", fullName(listOf("Jeff")))
    assertEquals("Jeff Hudson", fullName(listOf("Jeff", "Hudson")))
    assertEquals("Jeff Ryder Hudson", fullName(listOf("Jeff", "Ryder", "Hudson")))
  }
}
