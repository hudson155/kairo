package kairo.dateRange

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.Period
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@JsonIncludeProperties("start", "endInclusive")
public data class LocalDateRange private constructor(
  @all:JsonProperty("start")
  override val start: LocalDate,
  @all:JsonProperty("endInclusive")
  override val endInclusive: LocalDate,
) : ClosedRange<LocalDate>, OpenEndRange<LocalDate> {
  override val endExclusive: LocalDate = endInclusive.plusDays(1)

  init {
    require(start <= endExclusive) { "Invalid range." }
  }

  public val size: Long
    get() = ChronoUnit.DAYS.between(start, endInclusive) + 1

  override fun isEmpty(): Boolean =
    start > endInclusive

  @Suppress("ConvertTwoComparisonsToRangeCheck")
  override fun contains(value: LocalDate): Boolean =
    value >= start && value <= endInclusive

  public fun toList(increment: Period = Period.ofDays(1)): List<LocalDate> =
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
    public fun inclusive(start: LocalDate, endInclusive: LocalDate): LocalDateRange =
      LocalDateRange(start, endInclusive)

    public fun exclusive(start: LocalDate, endExclusive: LocalDate): LocalDateRange =
      LocalDateRange(start, endExclusive.minusDays(1))

    public fun forYear(year: Year): LocalDateRange {
      val start = year.atDay(1)
      val endExclusive = start.plusYears(1)
      return exclusive(start = start, endExclusive = endExclusive)
    }

    public fun forMonth(month: YearMonth): LocalDateRange {
      val start = month.atDay(1)
      val endExclusive = start.plusMonths(1)
      return exclusive(start = start, endExclusive = endExclusive)
    }

    public fun forDate(date: LocalDate): LocalDateRange =
      exclusive(start = date, endExclusive = date.plusDays(1))
  }
}

public operator fun LocalDate.rangeTo(that: LocalDate): LocalDateRange =
  LocalDateRange.inclusive(this, that)

public operator fun LocalDate.rangeUntil(that: LocalDate): LocalDateRange =
  LocalDateRange.exclusive(this, that)

public fun Year.toLocalDateRange(): LocalDateRange =
  LocalDateRange.forYear(this)

public fun YearMonth.toLocalDateRange(): LocalDateRange =
  LocalDateRange.forMonth(this)

public fun LocalDate.toLocalDateRange(): LocalDateRange =
  LocalDateRange.forDate(this)
