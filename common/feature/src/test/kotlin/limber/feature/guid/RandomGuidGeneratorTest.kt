package limber.feature.guid

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RandomGuidGeneratorTest {
  private val guidGenerator: RandomGuidGenerator = RandomGuidGenerator()

  // There's no great way to test randomness, so we just ensure there are no duplicates over 100,000 generations.
  @Test
  fun test() {
    List(100_000) { guidGenerator.generate() }.distinct().size.shouldBe(100_000)
  }
}
