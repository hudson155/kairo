package kairo.protectedString

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringTest {
  @Test
  fun value() {
    ProtectedString("1").value
      .shouldBe("1")
  }

  @Test
  fun `equals method`() {
    ProtectedString("1")
      .shouldBe(ProtectedString("1"))
    ProtectedString("1")
      .shouldNotBe(ProtectedString("2"))
  }

  @Test
  fun `hashCode method`() {
    ProtectedString("1").hashCode()
      .shouldBe("1".hashCode())
    ProtectedString("1").hashCode()
      .shouldNotBe("2".hashCode())
  }

  @Test
  fun `toString method`() {
    ProtectedString("1").toString()
      .shouldBe("ProtectedString(value='REDACTED')")
  }
}
