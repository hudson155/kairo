package io.limberapp.common.util.url

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class UrlTest {
  @Test
  fun enc() {
    assertEquals("Jeff+Hudson", enc("Jeff Hudson"))
    assertEquals("jeff.hudson%40limberapp.io", enc("jeff.hudson@limberapp.io"))
  }

  @Test
  fun slugify() {
    assertEquals("jeff-hudson", slugify("Jeff Hudson"))
    assertEquals("jean-francois", slugify("Jean-François"))
    assertEquals("jeff-hudson-limberapp-io", slugify("jeff.hudson@limberapp.io"))
    assertEquals("1-2-3", slugify("  1@  -- 2!@#$%^&*()3.."))
  }

  @Test
  fun href() {
    assertEquals("/foo", href("/foo"))
    assertEquals("/foo?q1=v1&q2=v2&q1=v3", href(
        path = "/foo",
        queryParams = listOf("q1" to "v1", "q2" to "v2", "q1" to "v3"),
    ))
  }
}
