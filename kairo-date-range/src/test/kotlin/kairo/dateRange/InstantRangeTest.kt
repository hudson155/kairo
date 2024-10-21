package kairo.dateRange

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InstantRangeTest {
  private val emptyRange: InstantRange =
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2023-11-13T19:44:32.123456789Z"),
    )

  private val singleNanosecondRange: InstantRange =
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2023-11-13T19:44:32.123456790Z"),
    )

  private val typicalRange: InstantRange =
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2024-01-04T00:01:59.567890123Z"),
    )

  @Test
  fun `exclusive factory`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      InstantRange.exclusive(
        start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
        endExclusive = Instant.parse("2023-11-13T19:44:32.123456788Z"),
      )
    }
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2023-11-13T19:44:32.123456789Z"),
    ).should { instantRange ->
      instantRange.start.shouldBe(Instant.parse("2023-11-13T19:44:32.123456789Z"))
      instantRange.endExclusive.shouldBe(Instant.parse("2023-11-13T19:44:32.123456789Z"))
    }
    InstantRange.exclusive(
      start = Instant.parse("2023-11-13T19:44:32.123456789Z"),
      endExclusive = Instant.parse("2024-01-04T00:01:59.567890123Z"),
    ).should { instantRange ->
      instantRange.start.shouldBe(Instant.parse("2023-11-13T19:44:32.123456789Z"))
      instantRange.endExclusive.shouldBe(Instant.parse("2024-01-04T00:01:59.567890123Z"))
    }
  }

  @Test
  fun isEmpty(): Unit = runTest {
    emptyRange.isEmpty().shouldBeTrue()
    singleNanosecondRange.isEmpty().shouldBeFalse()
    typicalRange.isEmpty().shouldBeFalse()
  }

  @Test
  fun contains(): Unit = runTest {
    (Instant.parse("2023-11-13T19:44:32.123456789Z") in emptyRange).shouldBeFalse()

    (Instant.parse("2023-11-13T19:44:32.123456788Z") in singleNanosecondRange).shouldBeFalse()
    (Instant.parse("2023-11-13T19:44:32.123456789Z") in singleNanosecondRange).shouldBeTrue()
    (Instant.parse("2023-11-13T19:44:32.123456790Z") in singleNanosecondRange).shouldBeFalse()

    (Instant.parse("2023-11-13T19:44:32.123456788Z") in typicalRange).shouldBeFalse()
    (Instant.parse("2023-11-13T19:44:32.123456789Z") in typicalRange).shouldBeTrue()
    (Instant.parse("2023-11-13T19:44:32.123456790Z") in typicalRange).shouldBeTrue()
    (Instant.parse("2024-01-04T00:01:59.567890122Z") in typicalRange).shouldBeTrue()
    (Instant.parse("2024-01-04T00:01:59.567890121Z") in typicalRange).shouldBeTrue()
    (Instant.parse("2024-01-04T00:01:59.567890123Z") in typicalRange).shouldBeFalse()
  }

  @Test
  fun forYear(): Unit = runTest {
    InstantRange.forYear(Year.of(2024))
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-01-01T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2025-01-01T00:00:00.000000000Z"),
        ),
      )
  }

  @Test
  fun forMonth(): Unit = runTest {
    InstantRange.forMonth(YearMonth.of(2024, Month.FEBRUARY))
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-02-01T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2024-03-01T00:00:00.000000000Z"),
        ),
      )
  }

  @Test
  fun forDate(): Unit = runTest {
    InstantRange.forDate(LocalDate.of(2024, Month.FEBRUARY, 29))
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-02-29T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2024-03-01T00:00:00.000000000Z"),
        ),
      )
  }

  @Test
  fun rangeUntil(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      Instant.parse("2023-11-13T19:44:32.123456789Z")..<Instant.parse("2023-11-13T19:44:32.123456788Z")
    }
    (Instant.parse("2023-11-13T19:44:32.123456789Z")..<Instant.parse("2023-11-13T19:44:32.123456789Z"))
      .shouldBe(emptyRange)
    (Instant.parse("2023-11-13T19:44:32.123456789Z")..<Instant.parse("2023-11-13T19:44:32.123456790Z"))
      .shouldBe(singleNanosecondRange)
    (Instant.parse("2023-11-13T19:44:32.123456789Z")..<Instant.parse("2024-01-04T00:01:59.567890123Z"))
      .shouldBe(typicalRange)
  }

  @Test
  fun `year toInstantRange`(): Unit = runTest {
    Year.of(2024).toInstantRange()
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-01-01T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2025-01-01T00:00:00.000000000Z"),
        ),
      )
  }

  @Test
  fun `yearMonth toInstantRange`(): Unit = runTest {
    YearMonth.of(2024, Month.FEBRUARY).toInstantRange()
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-02-01T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2024-03-01T00:00:00.000000000Z"),
        ),
      )
  }

  @Test
  fun `localDate toInstantRange`(): Unit = runTest {
    LocalDate.of(2024, Month.FEBRUARY, 29).toInstantRange()
      .shouldBe(
        InstantRange.exclusive(
          start = Instant.parse("2024-02-29T00:00:00.000000000Z"),
          endExclusive = Instant.parse("2024-03-01T00:00:00.000000000Z"),
        ),
      )
  }
}
