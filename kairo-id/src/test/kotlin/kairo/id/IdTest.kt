package kairo.id

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class IdTest {
  @Test
  fun `equals method`(): Unit =
    runTest {
      Id.parse("user_2eDS1sMt").shouldBe(Id.parse("user_2eDS1sMt"))
      Id.parse("user_2eDS1sMt").shouldNotBe(Id.parse("user_X64k1rU2"))
    }

  @Test
  fun `hashCode method`(): Unit =
    runTest {
      Id.parse("user_2eDS1sMt").hashCode().shouldBe(Id.parse("user_2eDS1sMt").hashCode())
      Id.parse("user_2eDS1sMt").hashCode().shouldBe("user_2eDS1sMt".hashCode())
    }

  @Test
  fun `toString method`(): Unit =
    runTest {
      val id = Id.parse("user_2eDS1sMt")
      id.toString().shouldBe("user_2eDS1sMt")
    }

  @Test
  fun `parse, valid`(): Unit =
    runTest {
      shouldNotThrowAny { Id.parse("user_2eDS1sMt") }
      shouldNotThrowAny { Id.parse("first_second_third_v2_k_2eDS1sMt") }
      shouldNotThrowAny { Id.parse("user_0") }
    }

  @Test
  fun `parse, invalid`(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> { Id.parse("user") }
      shouldThrow<IllegalArgumentException> { Id.parse("0_user") }
      shouldThrow<IllegalArgumentException> { Id.parse("user__2eDS1sMt") }
      shouldThrow<IllegalArgumentException> { Id.parse("user-2eDS1sMt") }
      shouldThrow<IllegalArgumentException> { Id.parse("User_2eDS1sMt") }
      shouldThrow<IllegalArgumentException> { Id.parse("usër_2eDS1sMt") }
      shouldThrow<IllegalArgumentException> { Id.parse("user_2eDS1!sMt") }
    }
}
