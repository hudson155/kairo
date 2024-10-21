package kairo.dateRange

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LocalDateRangeTest {
  private val emptyRange: LocalDateRange =
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2023-11-12"),
    )

  private val singleDateRange: LocalDateRange =
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2023-11-13"),
    )

  private val typicalRange: LocalDateRange =
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2024-01-04"),
    )

  @Test
  fun `inclusive factory`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      LocalDateRange.inclusive(
        start = LocalDate.parse("2023-11-13"),
        endInclusive = LocalDate.parse("2023-11-11"),
      )
    }
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2023-11-12"),
    ).should { localDateRange ->
      localDateRange.start.shouldBe(LocalDate.parse("2023-11-13"))
      localDateRange.endInclusive.shouldBe(LocalDate.parse("2023-11-12"))
      localDateRange.endExclusive.shouldBe(LocalDate.parse("2023-11-13"))
    }
    LocalDateRange.inclusive(
      start = LocalDate.parse("2023-11-13"),
      endInclusive = LocalDate.parse("2024-01-04"),
    ).should { localDateRange ->
      localDateRange.start.shouldBe(LocalDate.parse("2023-11-13"))
      localDateRange.endInclusive.shouldBe(LocalDate.parse("2024-01-04"))
      localDateRange.endExclusive.shouldBe(LocalDate.parse("2024-01-05"))
    }
  }

  @Test
  fun `exclusive factory`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      LocalDateRange.exclusive(
        start = LocalDate.parse("2023-11-13"),
        endExclusive = LocalDate.parse("2023-11-12"),
      )
    }
    LocalDateRange.exclusive(
      start = LocalDate.parse("2023-11-13"),
      endExclusive = LocalDate.parse("2023-11-13"),
    ).should { localDateRange ->
      localDateRange.start.shouldBe(LocalDate.parse("2023-11-13"))
      localDateRange.endInclusive.shouldBe(LocalDate.parse("2023-11-12"))
      localDateRange.endExclusive.shouldBe(LocalDate.parse("2023-11-13"))
    }
    LocalDateRange.exclusive(
      start = LocalDate.parse("2023-11-13"),
      endExclusive = LocalDate.parse("2024-01-05"),
    ).should { localDateRange ->
      localDateRange.start.shouldBe(LocalDate.parse("2023-11-13"))
      localDateRange.endInclusive.shouldBe(LocalDate.parse("2024-01-04"))
      localDateRange.endExclusive.shouldBe(LocalDate.parse("2024-01-05"))
    }
  }

  @Test
  fun size(): Unit = runTest {
    emptyRange.size.shouldBe(0L)
    singleDateRange.size.shouldBe(1L)
    typicalRange.size.shouldBe(53L)
  }

  @Test
  fun isEmpty(): Unit = runTest {
    emptyRange.isEmpty().shouldBeTrue()
    singleDateRange.isEmpty().shouldBeFalse()
    typicalRange.isEmpty().shouldBeFalse()
  }

  @Test
  fun contains(): Unit = runTest {
    (LocalDate.parse("2023-11-12") in emptyRange).shouldBeFalse()
    (LocalDate.parse("2023-11-13") in emptyRange).shouldBeFalse()

    (LocalDate.parse("2023-11-12") in singleDateRange).shouldBeFalse()
    (LocalDate.parse("2023-11-13") in singleDateRange).shouldBeTrue()
    (LocalDate.parse("2023-11-14") in singleDateRange).shouldBeFalse()

    (LocalDate.parse("2023-11-12") in typicalRange).shouldBeFalse()
    (LocalDate.parse("2023-11-13") in typicalRange).shouldBeTrue()
    (LocalDate.parse("2023-11-14") in typicalRange).shouldBeTrue()
    (LocalDate.parse("2024-01-03") in typicalRange).shouldBeTrue()
    (LocalDate.parse("2024-01-04") in typicalRange).shouldBeTrue()
    (LocalDate.parse("2024-01-05") in typicalRange).shouldBeFalse()
  }

  @Test
  fun toList(): Unit = runTest {
    emptyRange.toList().shouldBeEmpty()
    emptyRange.toList(increment = Period.ofWeeks(1)).shouldBeEmpty()

    singleDateRange.toList().shouldContainExactly(LocalDate.parse("2023-11-13"))
    singleDateRange.toList(increment = Period.ofWeeks(1)).shouldContainExactly(LocalDate.parse("2023-11-13"))

    typicalRange.toList().should { list ->
      list.firstOrNull().shouldBe(LocalDate.parse("2023-11-13"))
      list.lastOrNull().shouldBe(LocalDate.parse("2024-01-04"))
      list.shouldHaveSize(53)
    }
    typicalRange.toList(increment = Period.ofWeeks(1)).should { list ->
      list.firstOrNull().shouldBe(LocalDate.parse("2023-11-13"))
      list.lastOrNull().shouldBe(LocalDate.parse("2024-01-01"))
      list.shouldHaveSize(8)
    }
  }

  @Test
  fun forYear(): Unit = runTest {
    LocalDateRange.forYear(Year.of(2024))
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-01-01"),
          endInclusive = LocalDate.parse("2024-12-31"),
        ),
      )
  }

  @Test
  fun forMonth(): Unit = runTest {
    LocalDateRange.forMonth(YearMonth.of(2024, Month.FEBRUARY))
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-02-01"),
          endInclusive = LocalDate.parse("2024-02-29"),
        ),
      )
  }

  @Test
  fun forDate(): Unit = runTest {
    LocalDateRange.forDate(LocalDate.of(2024, Month.FEBRUARY, 29))
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-02-29"),
          endInclusive = LocalDate.parse("2024-02-29"),
        ),
      )
  }

  @Test
  fun rangeTo(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      LocalDate.parse("2023-11-13")..LocalDate.parse("2023-11-11")
    }
    (LocalDate.parse("2023-11-13")..LocalDate.parse("2023-11-12"))
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2023-11-13"),
          endInclusive = LocalDate.parse("2023-11-12"),
        ),
      )
    (LocalDate.parse("2023-11-13")..LocalDate.parse("2024-01-04"))
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2023-11-13"),
          endInclusive = LocalDate.parse("2024-01-04"),
        ),
      )
  }

  @Test
  fun rangeUntil(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      LocalDate.parse("2023-11-13")..<LocalDate.parse("2023-11-12")
    }
    (LocalDate.parse("2023-11-13")..<LocalDate.parse("2023-11-13"))
      .shouldBe(
        LocalDateRange.exclusive(
          start = LocalDate.parse("2023-11-13"),
          endExclusive = LocalDate.parse("2023-11-13"),
        ),
      )
    (LocalDate.parse("2023-11-13")..<LocalDate.parse("2024-01-05"))
      .shouldBe(
        LocalDateRange.exclusive(
          start = LocalDate.parse("2023-11-13"),
          endExclusive = LocalDate.parse("2024-01-05"),
        ),
      )
  }

  @Test
  fun `year toLocalDateRange`(): Unit = runTest {
    Year.of(2024).toLocalDateRange()
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-01-01"),
          endInclusive = LocalDate.parse("2024-12-31"),
        ),
      )
  }

  @Test
  fun `yearMonth toLocalDateRange`(): Unit = runTest {
    YearMonth.of(2024, Month.FEBRUARY).toLocalDateRange()
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-02-01"),
          endInclusive = LocalDate.parse("2024-02-29"),
        ),
      )
  }

  @Test
  fun `localDate toLocalDateRange`(): Unit = runTest {
    LocalDate.of(2024, Month.FEBRUARY, 29).toLocalDateRange()
      .shouldBe(
        LocalDateRange.inclusive(
          start = LocalDate.parse("2024-02-29"),
          endInclusive = LocalDate.parse("2024-02-29"),
        ),
      )
  }
}
