package kairo.time

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeParseException
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LenientTimeParserTest {
  @Test
  fun `year, only year`(): Unit = runTest {
    LenientTimeParser.year("2023")
      .shouldBe(Year.of(2023))
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.year("2023T")
    }
  }

  @Test
  fun `year, additional`(): Unit = runTest {
    LenientTimeParser.year("2023-12")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084577243")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `year, time zones`(): Unit = runTest {
    LenientTimeParser.year("2023-12-31T20:51:58.084577243")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084577243Z")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084577243Z[America/Edmonton]")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(Year.of(2023))
    LenientTimeParser.year("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Year.of(2023))
  }

  @Test
  fun `yearMonth, insufficient`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023")
      .shouldBe(YearMonth.of(2023, Month.JANUARY))
  }

  @Test
  fun `yearMonth, only yearMonth`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.yearMonth("2023-12T")
    }
  }

  @Test
  fun `yearMonth, additional`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `yearMonth, time zones`(): Unit = runTest {
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243Z")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243Z[America/Edmonton]")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
    LenientTimeParser.yearMonth("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(YearMonth.of(2023, Month.DECEMBER))
  }

  @Test
  fun `localDate, insufficient`(): Unit = runTest {
    LenientTimeParser.localDate("2023")
      .shouldBe(LocalDate.of(2023, Month.JANUARY, 1))
    LenientTimeParser.localDate("2023-12")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 1))
  }

  @Test
  fun `localDate, only localDate`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T")
  }

  @Test
  fun `localDate, additional`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `localDate, time zones`(): Unit = runTest {
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243Z")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243Z[America/Edmonton]")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
    LenientTimeParser.localDate("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(LocalDate.of(2023, Month.DECEMBER, 31))
  }

  @Test
  fun `instant, insufficient`(): Unit = runTest {
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.instant("2023")
    }
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.instant("2023T")
    }
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.instant("2023-12")
    }
    shouldThrow<DateTimeParseException> {
      LenientTimeParser.instant("2023-12T")
    }
  }

  @Test
  fun `instant, only instant`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31")
      .shouldBe(Instant.parse("2023-12-31T00:00:00Z"))
    LenientTimeParser.instant("2023-12-31T")
      .shouldBe(Instant.parse("2023-12-31T00:00:00Z"))
    LenientTimeParser.instant("2023-12-31T20")
      .shouldBe(Instant.parse("2023-12-31T20:00:00Z"))
    LenientTimeParser.instant("2023-12-31T20:51")
      .shouldBe(Instant.parse("2023-12-31T20:51:00Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58")
      .shouldBe(Instant.parse("2023-12-31T20:51:58Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.085")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.085Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
  }

  @Test
  fun `instant, time zones`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243Z")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243Z[America/Edmonton]")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
  }

  @Test
  fun `iso-8601, seconds`(): Unit = runTest {
    LenientTimeParser.instant("1704055918")
      .shouldBe(Instant.parse("2023-12-31T20:51:58Z"))
  }

  @Test
  fun `iso-8601, milliseconds`(): Unit = runTest {
    LenientTimeParser.instant("1704055918084")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084Z"))
  }
}
