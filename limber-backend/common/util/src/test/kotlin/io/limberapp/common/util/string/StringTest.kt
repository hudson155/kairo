package io.limberapp.common.util.string

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class StringTest {
  @Test
  fun joinNames() {
    assertEquals("", fullName())
    assertEquals("Jeff", fullName("Jeff"))
    assertEquals("Jeff Hudson", fullName("Jeff", "Hudson"))
    assertEquals("Jeff Ryder Hudson", fullName("Jeff", "Ryder", "Hudson"))
  }
}
