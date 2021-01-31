package io.limberapp.common.util.uuid

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DeterministicUuidGeneratorTest {
  private val uuidGenerator: DeterministicUuidGenerator = DeterministicUuidGenerator()

  @BeforeEach
  fun beforeEach() {
    uuidGenerator.reset()
  }

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
    val guid1 = uuidGenerator.generate()
    uuidGenerator.reset()
    val guid2 = uuidGenerator.generate()
    assertEquals(guid1, guid2)
  }
}
