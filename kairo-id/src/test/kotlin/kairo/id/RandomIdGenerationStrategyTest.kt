package kairo.id

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RandomIdGenerationStrategyTest {
  private val userIdGenerator: UserIdGenerator =
    UserIdGenerator(RandomIdGenerationStrategy(length = 22))

  @Test
  fun one(): Unit =
    runTest {
      val userId = userIdGenerator.generate()
      userId.toString().shouldNotBe("UserId(value=user_0000000000000000000000)")
      userId.toString().shouldMatch(Regex("UserId[(]value=user_[a-zA-Z0-9]{22}[)]"))
    }

  /**
   * There's no great way to test randomness, so we just ensure there are no duplicates over 100,000 generations.
   */
  @Test
  fun randomness(): Unit =
    runTest {
      val userIds = List(100_000) { userIdGenerator.generate() }
      userIds.distinct().shouldHaveSize(100_000)
    }
}
