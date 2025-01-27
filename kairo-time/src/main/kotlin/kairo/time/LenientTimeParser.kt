package kairo.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
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

/**
 * Parses times leniently, meaning not all fields have to be present.
 * Stricter time parsing should be used when possible.
 */
public object LenientTimeParser {
  private val year: DateTimeFormatter = build(SmallestRequiredUnit.Year)

  private val yearMonth: DateTimeFormatter = build(SmallestRequiredUnit.Year)

  private val localDate: DateTimeFormatter = build(SmallestRequiredUnit.Year)

  private val localDateTime: DateTimeFormatter = build(SmallestRequiredUnit.Day)

  private val zonedDateTime: DateTimeFormatter = build(SmallestRequiredUnit.Day)

  public fun year(string: String): Year =
    Year.parse(string, year)

  public fun yearMonth(string: String): YearMonth =
    YearMonth.parse(string, yearMonth)

  public fun localDate(string: String): LocalDate =
    LocalDate.parse(string, localDate)

  public fun instant(string: String): Instant {
    try {
      return ZonedDateTime.parse(string, zonedDateTime).toInstant()
    } catch (zonedException: DateTimeParseException) {
      try {
        return LocalDateTime.parse(string, localDateTime).toInstant(ZoneOffset.UTC)
      } catch (localException: DateTimeParseException) {
        zonedException.addSuppressed(localException)
        throw zonedException
      }
    }
  }

  private enum class SmallestRequiredUnit(
    val monthIsRequired: Boolean,
    val dayIsRequired: Boolean,
  ) {
    Year(monthIsRequired = false, dayIsRequired = false),
    Month(monthIsRequired = true, dayIsRequired = false),
    Day(monthIsRequired = true, dayIsRequired = true),
  }

  private fun build(smallestRequiredUnit: SmallestRequiredUnit): DateTimeFormatter =
    DateTimeFormatterBuilder().apply {
      appendValue(ChronoField.YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
      append(required = smallestRequiredUnit.monthIsRequired) {
        appendLiteral('-')
        appendValue(ChronoField.MONTH_OF_YEAR, 2)
        append(required = smallestRequiredUnit.dayIsRequired) {
          appendLiteral('-')
          appendValue(ChronoField.DAY_OF_MONTH, 2)
          append(required = false) {
            appendLiteral('T')
            append(required = false) {
              appendValue(ChronoField.HOUR_OF_DAY, 2)
              append(required = false) {
                appendLiteral(':')
                appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                append(required = false) {
                  appendLiteral(':')
                  appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                  append(required = false) {
                    appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
                  }
                }
              }
            }
          }
        }
      }
      append(required = false) {
        appendOffsetId()
      }
      append(required = false) {
        appendLiteral('[')
        parseCaseSensitive()
        appendZoneRegionId()
        appendLiteral(']')
      }
      parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
      parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
      parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
    }.toFormatter()

  private fun DateTimeFormatterBuilder.append(required: Boolean, block: DateTimeFormatterBuilder.() -> Unit) {
    if (!required) optionalStart()
    block()
    if (!required) optionalEnd()
  }
}
