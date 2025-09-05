package kairo.id

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldMatch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RandomIdTest {
  @Test
  fun format(): Unit =
    runTest {
      val userId = UserId.random()
      userId.toString().shouldMatch(Regex("UserId[(]value=user_[a-zA-Z0-9]{15}[)]"))
    }

  /**
   * There's no great way to test randomness, so we just ensure there are no duplicates over 100,000 generations.
   */
  @Test
  fun randomness(): Unit =
    runTest {
      val userIds = List(100_000) { UserId.random() }
      userIds.distinct().shouldHaveSize(100_000)
    }
}
