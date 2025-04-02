package kairo.time

import java.time.Month

public object LenientMonthParser {
  private val map: Map<String, Month> =
    mapOf(
      "jan" to Month.JANUARY,
      "jan." to Month.JANUARY,
      "january" to Month.JANUARY,
      "feb" to Month.FEBRUARY,
      "feb." to Month.FEBRUARY,
      "february" to Month.FEBRUARY,
      "mar" to Month.MARCH,
      "mar." to Month.MARCH,
      "march" to Month.MARCH,
      "apr" to Month.APRIL,
      "apr." to Month.APRIL,
      "april" to Month.APRIL,
      "may" to Month.MAY,
      "may." to Month.MAY,
      "jun" to Month.JUNE,
      "jun." to Month.JUNE,
      "june" to Month.JUNE,
      "jul" to Month.JULY,
      "jul." to Month.JULY,
      "july" to Month.JULY,
      "aug" to Month.AUGUST,
      "aug." to Month.AUGUST,
      "august" to Month.AUGUST,
      "sep" to Month.SEPTEMBER,
      "sep." to Month.SEPTEMBER,
      "sept" to Month.SEPTEMBER,
      "sept." to Month.SEPTEMBER,
      "september" to Month.SEPTEMBER,
      "oct" to Month.OCTOBER,
      "oct." to Month.OCTOBER,
      "october" to Month.OCTOBER,
      "nov" to Month.NOVEMBER,
      "nov." to Month.NOVEMBER,
      "november" to Month.NOVEMBER,
      "dec" to Month.DECEMBER,
      "dec." to Month.DECEMBER,
      "december" to Month.DECEMBER,
    )

  public fun parse(string: String): Month =
    requireNotNull(map[string.lowercase()]) { "Invalid month: $string." }
}
