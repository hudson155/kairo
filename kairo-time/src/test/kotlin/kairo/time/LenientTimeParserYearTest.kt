package kairo.time

import io.kotest.matchers.shouldBe
import java.time.Year
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LenientTimeParserYearTest {
  @Test
  fun `epoch second`(): Unit = runTest {
    LenientTimeParser.year("1704055918")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `epoch milli`(): Unit = runTest {
    LenientTimeParser.year("1704055918084")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun happy(): Unit = runTest {
    LenientTimeParser.year("2023")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (month)`(): Unit = runTest {
    LenientTimeParser.year("2023-12")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (day)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (t)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (hour)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (minute)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (second)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (milli)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (nano)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (utc)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243Z")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (offset hour)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243-07")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (offset minute)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `additional fields (offset region)`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `space instead of t`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31 20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Year.of(2023))
  }
}
