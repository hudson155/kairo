package kairo.id

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

internal class RandomIdGenerationStrategyTest {
  @Serializable
  @JvmInline
  value class UserId(val value: Id) {
    init {
      require(value.toString().matches(regex))
    }

    override fun toString(): String =
      value.toString()

    companion object {
      val regex: Regex = Id.regex(prefix = Regex("user"))
    }
  }

  class UserIdGenerator(
    strategy: IdGenerationStrategy,
  ) : IdGenerator<UserId>(strategy, prefix = "user") {
    override fun wrap(id: Id): UserId =
      UserId(id)
  }

  private val userIdGenerator: UserIdGenerator =
    UserIdGenerator(RandomIdGenerationStrategy(length = 22))

  @Test
  fun one() {
    val userId = userIdGenerator.generate()
    userId.toString().shouldNotBe("user_0000000000000000000000")
    userId.toString().shouldMatch(Regex("user_[a-zA-Z0-9]{22}"))
  }

  /**
   * There's no great way to test randomness, so we just ensure there are no duplicates over 100,000 generations.
   */
  @Test
  fun randomness() {
    val userIds = List(100_000) { userIdGenerator.generate() }
    userIds.distinct().shouldHaveSize(100_000)
  }
}
