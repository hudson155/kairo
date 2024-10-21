package kairo.dateRange

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class YearMonthRangeTest {
  private val emptyRange: YearMonthRange =
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2022-06"),
    )

  private val singleDateRange: YearMonthRange =
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2022-07"),
    )

  private val typicalRange: YearMonthRange =
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2024-03"),
    )

  @Test
  fun `inclusive factory`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      YearMonthRange.inclusive(
        start = YearMonth.parse("2022-07"),
        endInclusive = YearMonth.parse("2022-05"),
      )
    }
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2022-06"),
    ).should { yearMonthRange ->
      yearMonthRange.start.shouldBe(YearMonth.parse("2022-07"))
      yearMonthRange.endInclusive.shouldBe(YearMonth.parse("2022-06"))
      yearMonthRange.endExclusive.shouldBe(YearMonth.parse("2022-07"))
    }
    YearMonthRange.inclusive(
      start = YearMonth.parse("2022-07"),
      endInclusive = YearMonth.parse("2024-03"),
    ).should { yearMonthRange ->
      yearMonthRange.start.shouldBe(YearMonth.parse("2022-07"))
      yearMonthRange.endInclusive.shouldBe(YearMonth.parse("2024-03"))
      yearMonthRange.endExclusive.shouldBe(YearMonth.parse("2024-04"))
    }
  }

  @Test
  fun `exclusive factory`(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      YearMonthRange.exclusive(
        start = YearMonth.parse("2022-07"),
        endExclusive = YearMonth.parse("2022-06"),
      )
    }
    YearMonthRange.exclusive(
      start = YearMonth.parse("2022-07"),
      endExclusive = YearMonth.parse("2022-07"),
    ).should { yearMonthRange ->
      yearMonthRange.start.shouldBe(YearMonth.parse("2022-07"))
      yearMonthRange.endInclusive.shouldBe(YearMonth.parse("2022-06"))
      yearMonthRange.endExclusive.shouldBe(YearMonth.parse("2022-07"))
    }
    YearMonthRange.exclusive(
      start = YearMonth.parse("2022-07"),
      endExclusive = YearMonth.parse("2024-04"),
    ).should { yearMonthRange ->
      yearMonthRange.start.shouldBe(YearMonth.parse("2022-07"))
      yearMonthRange.endInclusive.shouldBe(YearMonth.parse("2024-03"))
      yearMonthRange.endExclusive.shouldBe(YearMonth.parse("2024-04"))
    }
  }

  @Test
  fun size(): Unit = runTest {
    emptyRange.size.shouldBe(0)
    singleDateRange.size.shouldBe(1)
    typicalRange.size.shouldBe(21)
  }

  @Test
  fun isEmpty(): Unit = runTest {
    emptyRange.isEmpty().shouldBeTrue()
    singleDateRange.isEmpty().shouldBeFalse()
    typicalRange.isEmpty().shouldBeFalse()
  }

  @Test
  fun contains(): Unit = runTest {
    (YearMonth.parse("2022-06") in emptyRange).shouldBeFalse()
    (YearMonth.parse("2022-07") in emptyRange).shouldBeFalse()

    (YearMonth.parse("2022-06") in singleDateRange).shouldBeFalse()
    (YearMonth.parse("2022-07") in singleDateRange).shouldBeTrue()
    (YearMonth.parse("2022-08") in singleDateRange).shouldBeFalse()

    (YearMonth.parse("2022-06") in typicalRange).shouldBeFalse()
    (YearMonth.parse("2022-07") in typicalRange).shouldBeTrue()
    (YearMonth.parse("2022-08") in typicalRange).shouldBeTrue()
    (YearMonth.parse("2024-02") in typicalRange).shouldBeTrue()
    (YearMonth.parse("2024-03") in typicalRange).shouldBeTrue()
    (YearMonth.parse("2024-04") in typicalRange).shouldBeFalse()
  }

  @Test
  fun toList(): Unit = runTest {
    emptyRange.toList().shouldBeEmpty()
    emptyRange.toList(increment = Period.ofYears(1)).shouldBeEmpty()

    singleDateRange.toList().shouldContainExactly(YearMonth.parse("2022-07"))
    singleDateRange.toList(increment = Period.ofYears(1)).shouldContainExactly(YearMonth.parse("2022-07"))

    typicalRange.toList().should { list ->
      list.firstOrNull().shouldBe(YearMonth.parse("2022-07"))
      list.lastOrNull().shouldBe(YearMonth.parse("2024-03"))
      list.shouldHaveSize(21)
    }
    typicalRange.toList(increment = Period.ofYears(1)).should { list ->
      list.firstOrNull().shouldBe(YearMonth.parse("2022-07"))
      list.lastOrNull().shouldBe(YearMonth.parse("2023-07"))
      list.shouldHaveSize(2)
    }
  }

  @Test
  fun forYear(): Unit = runTest {
    YearMonthRange.forYear(Year.of(2024))
      .shouldBe(
        YearMonthRange.inclusive(
          start = YearMonth.parse("2024-01"),
          endInclusive = YearMonth.parse("2024-12"),
        ),
      )
  }

  @Test
  fun forMonth(): Unit = runTest {
    YearMonthRange.forMonth(YearMonth.of(2024, Month.FEBRUARY))
      .shouldBe(
        YearMonthRange.inclusive(
          start = YearMonth.parse("2024-02"),
          endInclusive = YearMonth.parse("2024-02"),
        ),
      )
  }

  @Test
  fun rangeTo(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      YearMonth.parse("2022-07")..YearMonth.parse("2022-05")
    }
    (YearMonth.parse("2022-07")..YearMonth.parse("2022-06"))
      .shouldBe(emptyRange)
    (YearMonth.parse("2022-07")..YearMonth.parse("2022-07"))
      .shouldBe(singleDateRange)
    (YearMonth.parse("2022-07")..YearMonth.parse("2024-03"))
      .shouldBe(typicalRange)
  }

  @Test
  fun rangeUntil(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      YearMonth.parse("2022-07")..<YearMonth.parse("2022-06")
    }
    (YearMonth.parse("2022-07")..<YearMonth.parse("2022-07"))
      .shouldBe(emptyRange)
    (YearMonth.parse("2022-07")..<YearMonth.parse("2022-08"))
      .shouldBe(singleDateRange)
    (YearMonth.parse("2022-07")..<YearMonth.parse("2024-04"))
      .shouldBe(typicalRange)
  }

  @Test
  fun `year toYearMonthRange`(): Unit = runTest {
    Year.of(2024).toYearMonthRange()
      .shouldBe(
        YearMonthRange.inclusive(
          start = YearMonth.parse("2024-01"),
          endInclusive = YearMonth.parse("2024-12"),
        ),
      )
  }

  @Test
  fun `yearMonth toYearMonthRange`(): Unit = runTest {
    YearMonth.of(2024, Month.FEBRUARY).toYearMonthRange()
      .shouldBe(
        YearMonthRange.inclusive(
          start = YearMonth.parse("2024-02"),
          endInclusive = YearMonth.parse("2024-02"),
        ),
      )
  }
}
