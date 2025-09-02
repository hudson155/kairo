package kairo.id

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DeterministicIdGenerationStrategyTest {
  private val userIdGenerator: UserIdGenerator =
    UserIdGenerator(DeterministicIdGenerationStrategy(length = 22))

  @Test
  fun test(): Unit =
    runTest {
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000000)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000001)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000002)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000003)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000004)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000005)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000006)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000007)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000008)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000009)")
      userIdGenerator.generate().toString().shouldBe("UserId(value=user_0000000000000000000010)")
    }
}
