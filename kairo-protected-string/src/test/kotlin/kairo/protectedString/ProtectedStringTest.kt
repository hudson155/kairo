package kairo.protectedString

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringTest {
  @Test
  fun value(): Unit =
    runTest {
      ProtectedString("1").value
        .shouldBe("1")
    }

  @Test
  fun `equals method`(): Unit =
    runTest {
      ProtectedString("1")
        .shouldBe(ProtectedString("1"))
      ProtectedString("1")
        .shouldNotBe(ProtectedString("2"))
    }

  @Test
  fun `hashCode method`(): Unit =
    runTest {
      ProtectedString("1").hashCode()
        .shouldBe("1".hashCode())
      ProtectedString("1").hashCode()
        .shouldNotBe("2".hashCode())
    }

  @Test
  fun `toString method`(): Unit =
    runTest {
      ProtectedString("1").toString()
        .shouldBe("ProtectedString(value='REDACTED')")
    }
}
