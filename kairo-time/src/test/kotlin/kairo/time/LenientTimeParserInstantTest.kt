package kairo.time

import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LenientTimeParserInstantTest {
  @Test
  fun `epoch second`(): Unit = runTest {
    LenientTimeParser.instant("1704055918")
      .shouldBe(Instant.parse("2023-12-31T20:51:58Z"))
  }

  @Test
  fun `epoch milli`(): Unit = runTest {
    LenientTimeParser.instant("1704055918084")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084000000Z"))
  }

  @Test
  fun `missing month`(): Unit = runTest {
    LenientTimeParser.instant("2023")
      .shouldBe(Instant.parse("2023-01-01T00:00:00Z"))
  }

  @Test
  fun `missing day`(): Unit = runTest {
    LenientTimeParser.instant("2023-12")
      .shouldBe(Instant.parse("2023-12-01T00:00:00Z"))
  }

  @Test
  fun `missing t`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31")
      .shouldBe(Instant.parse("2023-12-31T00:00:00Z"))
  }

  @Test
  fun `missing hour`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T")
      .shouldBe(Instant.parse("2023-12-31T00:00:00Z"))
  }

  @Test
  fun `missing minute`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20")
      .shouldBe(Instant.parse("2023-12-31T20:00:00Z"))
  }

  @Test
  fun `missing second`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51")
      .shouldBe(Instant.parse("2023-12-31T20:51:00.Z"))
  }

  @Test
  fun `happy (to second)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58")
      .shouldBe(Instant.parse("2023-12-31T20:51:58Z"))
  }

  @Test
  fun `happy (to milli)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084000000Z"))
  }

  @Test
  fun `happy (to nano)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
  }

  @Test
  fun `happy (to utc)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243Z")
      .shouldBe(Instant.parse("2023-12-31T20:51:58.084577243Z"))
  }

  @Test
  fun `happy (to offset hour)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243-07")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
  }

  @Test
  fun `happy (to offset minute)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243-07:00")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
  }

  @Test
  fun `happy (to offset region)`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31T20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
  }

  @Test
  fun `space instead of t`(): Unit = runTest {
    LenientTimeParser.instant("2023-12-31 20:51:58.084577243-07:00[America/Edmonton]")
      .shouldBe(Instant.parse("2024-01-01T03:51:58.084577243Z"))
  }
}
