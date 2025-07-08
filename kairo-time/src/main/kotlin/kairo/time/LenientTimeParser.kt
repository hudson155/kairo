@file:Suppress("ForbiddenImport")

package kairo.time

import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalQueries

private val customIsoFormatter: DateTimeFormatter =
  DateTimeFormatterBuilder().apply {
    appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
    optional {
      appendLiteral('-')
      appendValue(ChronoField.MONTH_OF_YEAR, 2)
      optional {
        appendLiteral('-')
        appendValue(ChronoField.DAY_OF_MONTH, 2)
        optional {
          optional { appendLiteral(' ') }
          optional { appendLiteral('T') }
          optional { appendLiteral(' ') }
          optional {
            appendValue(ChronoField.HOUR_OF_DAY, 2)
            optional {
              appendLiteral(':')
              appendValue(ChronoField.MINUTE_OF_HOUR, 2)
              optional {
                appendLiteral(':')
                appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                optional {
                  appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                }
              }
            }
          }
        }
      }
    }
    optional {
      optional { appendOffset("+HH:MM", "Z") }
      optional { appendOffset("+HH", "Z") }
    }
    optional {
      appendLiteral('[')
      parseCaseSensitive()
      appendZoneRegionId()
      appendLiteral(']')
    }
    parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
    parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
    parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
  }.toFormatter()

/**
 * Parses times leniently, meaning not all fields have to be present.
 * Stricter time parsing should be used when possible.
 */
public object LenientTimeParser {
  internal abstract class Parser {
    abstract fun parse(string: String): TemporalAccessor?

    internal class Match(
      private val regex: Regex,
      private val extract: (matchResult: MatchResult) -> TemporalAccessor,
    ) : Parser() {
      override fun parse(string: String): TemporalAccessor? {
        val matchResult = regex.matchEntire(string) ?: return null
        return extract(matchResult)
      }
    }

    internal class Formatter(private val formatter: DateTimeFormatter) : Parser() {
      override fun parse(string: String): TemporalAccessor? =
        try {
          formatter.parse(string)
        } catch (_: DateTimeParseException) {
          null
        }
    }
  }

  private val parsers: List<Parser> =
    listOf(
      Parser.Match(Regex("[0-9]{13}")) { Instant.ofEpochMilli(it.value.toLong()).atZone(ZoneOffset.UTC) },
      Parser.Match(Regex("[0-9]{10}")) { Instant.ofEpochSecond(it.value.toLong()).atZone(ZoneOffset.UTC) },
      Parser.Formatter(customIsoFormatter)
    )

  public fun year(string: String): Year {
    val ta = parsers.firstNotNullOf { it.parse(string) }
    return Year.of(
      ta.get(ChronoField.YEAR),
    )
  }

  public fun yearMonth(string: String): YearMonth {
    val ta = parsers.firstNotNullOf { it.parse(string) }
    return YearMonth.of(
      ta.get(ChronoField.YEAR),
      ta.getDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.value),
    )
  }

  public fun localDate(string: String): LocalDate {
    val ta = parsers.firstNotNullOf { it.parse(string) }
    return LocalDate.of(
      ta.get(ChronoField.YEAR),
      ta.getDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.value),
      ta.getDefaulting(ChronoField.DAY_OF_MONTH, 1),
    )
  }

  public fun instant(string: String): Instant {
    val ta = parsers.firstNotNullOf { it.parse(string) }
    val zonedDateTime = ZonedDateTime.of(
      ta.get(ChronoField.YEAR),
      ta.getDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.value),
      ta.getDefaulting(ChronoField.DAY_OF_MONTH, 1),
      ta.getDefaulting(ChronoField.HOUR_OF_DAY, 0),
      ta.getDefaulting(ChronoField.MINUTE_OF_HOUR, 0),
      ta.getDefaulting(ChronoField.SECOND_OF_MINUTE, 0),
      ta.getDefaulting(ChronoField.NANO_OF_SECOND, 0),
      TemporalQueries.zone().let { ta.query(it) ?: ZoneOffset.UTC },
    )
    return zonedDateTime.toInstant()
  }
}

private fun DateTimeFormatterBuilder.optional(block: DateTimeFormatterBuilder.() -> Unit) {
  optionalStart()
  block()
  optionalEnd()
}

private fun TemporalAccessor.getDefaulting(field: ChronoField, default: Int): Int =
  if (isSupported(field)) get(field) else default
