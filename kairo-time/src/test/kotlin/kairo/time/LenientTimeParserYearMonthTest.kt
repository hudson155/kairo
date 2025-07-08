package kairo.time

import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LenientTimeParserYearMonthTest {
  @Test
  fun `epoch second`(): Unit = runTest {
    LenientTimeParser.yearMonth("1704055918")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `epoch milli`(): Unit = runTest {
    LenientTimeParser.yearMonth("1704055918084")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `missing month`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023")
      .shouldBe(YearMonth.of(2023, Month.JANUARY))
  }

  @Test
  fun happy(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (day)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (t)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (hour)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (minute)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (second)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (milli)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (nano)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (utc)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243Z")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (offset hour)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243-07")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (offset minute)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `additional fields (offset region)`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `space instead of t`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31 20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }
}
