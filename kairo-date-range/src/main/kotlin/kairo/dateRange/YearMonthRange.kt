package kairo.dateRange

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Month
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@JsonIncludeProperties("start", "endInclusive")
public data class YearMonthRange private constructor(
  @JsonProperty("start")
  @field:JsonProperty("start")
  override val start: YearMonth,
  @JsonProperty("endInclusive")
  @field:JsonProperty("endInclusive")
  override val endInclusive: YearMonth,
) : ClosedRange<YearMonth>, OpenEndRange<YearMonth> {
  override val endExclusive: YearMonth = endInclusive.plusMonths(1)

  init {
    require(start <= endExclusive) { "Invalid range." }
  }

  public val size: Long
    get() = ChronoUnit.MONTHS.between(start, endInclusive) + 1

  override fun isEmpty(): Boolean =
    start > endInclusive

  @Suppress("ConvertTwoComparisonsToRangeCheck")
  override fun contains(value: YearMonth): Boolean =
    value >= start && value <= endInclusive

  public fun toList(increment: Period = Period.ofMonths(1)): List<YearMonth> =
    buildList {
      var i = 0
      while (true) {
        val date = start.plus(increment.multipliedBy(i))
        if (date.isAfter(endInclusive)) break
        add(date)
        i++
      }
    }

  public companion object {
    public fun inclusive(start: YearMonth, endInclusive: YearMonth): YearMonthRange =
      YearMonthRange(start, endInclusive)

    public fun exclusive(start: YearMonth, endExclusive: YearMonth): YearMonthRange =
      YearMonthRange(start, endExclusive.minusMonths(1))

    public fun forYear(year: Year): YearMonthRange {
      val start = year.atMonth(Month.JANUARY)
      val endExclusive = start.plusYears(1)
      return exclusive(start = start, endExclusive = endExclusive)
    }

    public fun forMonth(month: YearMonth): YearMonthRange =
      exclusive(start = month, endExclusive = month.plusMonths(1))
  }
}

public operator fun YearMonth.rangeTo(that: YearMonth): YearMonthRange =
  YearMonthRange.inclusive(this, that)

public operator fun YearMonth.rangeUntil(that: YearMonth): YearMonthRange =
  YearMonthRange.exclusive(this, that)

public fun Year.toYearMonthRange(): YearMonthRange =
  YearMonthRange.forYear(this)

public fun YearMonth.toYearMonthRange(): YearMonthRange =
  YearMonthRange.forMonth(this)
