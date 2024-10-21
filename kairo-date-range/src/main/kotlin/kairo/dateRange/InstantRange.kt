package kairo.dateRange

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.ZoneOffset

@JsonIncludeProperties("start", "endExclusive")
public data class InstantRange private constructor(
  @JsonProperty("start")
  @field:JsonProperty("start")
  override val start: Instant,
  @JsonProperty("endExclusive")
  @field:JsonProperty("endExclusive")
  override val endExclusive: Instant,
) : OpenEndRange<Instant> {
  init {
    require(start <= endExclusive) { "Invalid date range." }
  }

  override fun isEmpty(): Boolean =
    start >= endExclusive

  @Suppress("ConvertTwoComparisonsToRangeCheck")
  override fun contains(value: Instant): Boolean =
    value >= start && value < endExclusive

  public companion object {
    public fun exclusive(start: Instant, endExclusive: Instant): InstantRange =
      InstantRange(start, endExclusive)

    public fun forYear(year: Year): InstantRange {
      val start = year.atDay(1).atStartOfDay(ZoneOffset.UTC)
      val endExclusive = start.plusYears(1)
      return exclusive(start = start.toInstant(), endExclusive = endExclusive.toInstant())
    }

    public fun forMonth(month: YearMonth): InstantRange {
      val start = month.atDay(1).atStartOfDay(ZoneOffset.UTC)
      val endExclusive = start.plusMonths(1)
      return exclusive(start = start.toInstant(), endExclusive = endExclusive.toInstant())
    }

    public fun forDate(date: LocalDate): InstantRange {
      val start = date.atStartOfDay(ZoneOffset.UTC)
      val endExclusive = start.plusDays(1)
      return exclusive(start = start.toInstant(), endExclusive = endExclusive.toInstant())
    }
  }
}

public operator fun Instant.rangeUntil(that: Instant): InstantRange =
  InstantRange.exclusive(this, that)

public fun Year.toInstantRange(): InstantRange =
  InstantRange.forYear(this)

public fun YearMonth.toInstantRange(): InstantRange =
  InstantRange.forMonth(this)

public fun LocalDate.toInstantRange(): InstantRange =
  InstantRange.forDate(this)
