package io.limberapp.common.util.uuid

import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeterministicUuidGeneratorTest {
  private val uuidGenerator: DeterministicUuidGenerator = DeterministicUuidGenerator()

  @Test
  fun `sanity check - should be valid`() {
    uuidGenerator.generate()
  }

  @Test
  fun `sanity check - should not generate duplicates`() {
    assertEquals(10_000, List(10_000) { uuidGenerator.generate() }.distinct().size)
  }

  @Test
  fun `sanity check - should be consistent`() {
    val list1 = uuidGenerator.generate()
    uuidGenerator.reset()
    val list2 = uuidGenerator.generate()
    assertEquals(list1, list2)
  }
}
