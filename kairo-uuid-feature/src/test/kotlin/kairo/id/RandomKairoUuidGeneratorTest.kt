package kairo.id

import io.kotest.matchers.shouldBe
import kairo.uuid.RandomKairoUuidGenerator
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RandomKairoUuidGeneratorTest {
  private val uuidGenerator: RandomKairoUuidGenerator =
    RandomKairoUuidGenerator()

  /**
   * There's no great way to test randomness, so we just ensure there are no duplicates over 100,000 generations.
   */
  @Test
  fun test(): Unit = runTest {
    List(100_000) { uuidGenerator.generate() }.distinct().size.shouldBe(100_000)
  }
}
