package io.limberapp.util.uuid

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Since this implementation of [UuidGenerator] is random, there's no great way to test it... But we
 * can at least do a few sanity checks.
 */
internal class RandomUuidGeneratorTest {
  private val uuidGenerator: UuidGenerator = RandomUuidGenerator()

  @Test
  fun `sanity check - should be valid`() {
    uuidGenerator.generate()
  }

  @Test
  fun `sanity check - should not generate duplicates`() {
    assertEquals(10_000, List(10_000) { uuidGenerator.generate() }.distinct().size)
  }
}
