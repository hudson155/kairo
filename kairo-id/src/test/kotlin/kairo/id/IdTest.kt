package kairo.id

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class IdTest {
  @Test
  fun `equals method`(): Unit =
    runTest {
      UserId("user_2eDS1sMt").shouldBe(UserId("user_2eDS1sMt"))
      UserId("user_2eDS1sMt").shouldNotBe(UserId("user_X64k1rU2"))
    }

  @Test
  fun `hashCode method`(): Unit =
    runTest {
      UserId("user_2eDS1sMt").hashCode().shouldBe(UserId("user_2eDS1sMt").hashCode())
      UserId("user_2eDS1sMt").hashCode().shouldBe("user_2eDS1sMt".hashCode())
    }

  @Test
  fun `toString method`(): Unit =
    runTest {
      val id = UserId("user_2eDS1sMt")
      id.toString().shouldBe("UserId(value=user_2eDS1sMt)")
    }

  @Test
  fun `validate (valid)`(): Unit =
    runTest {
      shouldNotThrowAny { UserId("user_2eDS1sMt") }
      shouldNotThrowAny { UserId("user_0") }
    }

  @Test
  fun `validate (invalid)`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> { UserId("user") }
        .shouldHaveMessage("Malformed user ID (value=user).")
      shouldThrow<IllegalArgumentException> { UserId("user_2_2eDS1sMt") }
        .shouldHaveMessage("Malformed user ID (value=user_2_2eDS1sMt).")
      shouldThrow<IllegalArgumentException> { UserId("user__2eDS1sMt") }
        .shouldHaveMessage("Malformed user ID (value=user__2eDS1sMt).")
      shouldThrow<IllegalArgumentException> { UserId("user-2eDS1sMt") }
        .shouldHaveMessage("Malformed user ID (value=user-2eDS1sMt).")
      shouldThrow<IllegalArgumentException> { UserId("User_2eDS1sMt") }
        .shouldHaveMessage("Malformed user ID (value=User_2eDS1sMt).")
      shouldThrow<IllegalArgumentException> { UserId("usër_2eDS1sMt") }
        .shouldHaveMessage("Malformed user ID (value=usër_2eDS1sMt).")
      shouldThrow<IllegalArgumentException> { UserId("user_2eDS1!sMt") }
        .shouldHaveMessage("Malformed user ID (value=user_2eDS1!sMt).")
    }
}
