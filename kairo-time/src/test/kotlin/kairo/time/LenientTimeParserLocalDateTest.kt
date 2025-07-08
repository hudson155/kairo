package kairo.time

import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LenientTimeParserLocalDateTest {
  @Test
  fun `epoch second`(): Unit = runTest {
    LenientTimeParser.localDate("1704055918")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `epoch milli`(): Unit = runTest {
    LenientTimeParser.localDate("1704055918084")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `missing month`(): Unit = runTest {
    LenientTimeParser.localDate("2023")
      .shouldBe(LocalDate.of(2023, Month.JANUARY, 1))
  }

  @Test
  fun `missing day`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 1))
  }

  @Test
  fun happy(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (t)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (hour)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (minute)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (second)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (milli)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (nano)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (utc)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243Z")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (offset hour)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243-07")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (offset minute)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `additional fields (offset region)`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `space instead of t`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31 20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }
}
