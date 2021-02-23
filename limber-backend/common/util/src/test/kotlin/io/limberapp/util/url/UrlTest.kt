package io.limberapp.util.url

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class UrlTest {
  @Test
  fun slugify() {
    assertEquals("jeff-hudson", slugify("Jeff Hudson"))
    assertEquals("jean-francois", slugify("Jean-François"))
    assertEquals("jeff-hudson-limberapp-io", slugify("jeff.hudson@limberapp.io"))
    assertEquals("1-2-3", slugify("  1@  -- 2!@#$%^&*()3.."))
  }
}
