package io.limberapp.common.sql.store

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class ResourceCacheTest {
  private val resourceCache: ResourceCache = ResourceCache()

  private val base: String = "store/test"

  @Test
  fun test() {
    val counter = withDelegateCounter {
      assertEquals("SELECT 1 FROM foo\n", resourceCache["$base/foo.sql"])
      assertEquals("SELECT 1 FROM bar\n", resourceCache["$base/bar.sql"])
      assertFailsWith<IllegalArgumentException> { resourceCache["$base/baz.sql"] }
      assertEquals("SELECT 1 FROM foo\n", resourceCache["$base/foo.sql"])
      assertEquals("SELECT 1 FROM foo\n", resourceCache["$base/foo.sql"])
    }
    assertEquals(mapOf("$base/foo.sql" to 1, "$base/bar.sql" to 1, "$base/baz.sql" to 1), counter)
  }

  @OptIn(ResourceManipulation::class)
  private fun withDelegateCounter(function: () -> Unit): Map<String, Int> =
      mutableMapOf<String, Int>().apply {
        try {
          getResource = { resourceName ->
            compute(resourceName) { _, value -> (value ?: 0) + 1 }
            DEFAULT_GET_RESOURCE(resourceName)
          }
          function()
        } finally {
          getResource = DEFAULT_GET_RESOURCE
        }
      }
}
