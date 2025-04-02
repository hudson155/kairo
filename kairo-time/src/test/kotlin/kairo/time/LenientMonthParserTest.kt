package kairo.time

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.Month
import org.junit.jupiter.api.Test

internal class LenientMonthParserTest {
  @Test
  fun invalid() {
    shouldThrow<IllegalArgumentException> {
      LenientMonthParser.parse("")
    }
    shouldThrow<IllegalArgumentException> {
      LenientMonthParser.parse("septemb")
    }
    shouldThrow<IllegalArgumentException> {
      LenientMonthParser.parse("september.")
    }
  }

  @Test
  fun september() {
    LenientMonthParser.parse("sep").shouldBe(Month.SEPTEMBER)
    LenientMonthParser.parse("sep.").shouldBe(Month.SEPTEMBER)
    LenientMonthParser.parse("sept").shouldBe(Month.SEPTEMBER)
    LenientMonthParser.parse("sept.").shouldBe(Month.SEPTEMBER)
    LenientMonthParser.parse("september").shouldBe(Month.SEPTEMBER)
  }
}
