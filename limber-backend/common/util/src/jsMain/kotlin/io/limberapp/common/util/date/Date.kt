package io.limberapp.common.util.date

import kotlinext.js.js
import kotlin.js.Date
import kotlin.math.abs
import kotlin.math.round

fun Date.prettyRelative() = prettyRelativeTimeDifference(reference = Date(), date = this)

/**
 * Calculates the difference between The [reference] date and the [date] provided, and outputs it as a string. Details
 * about implementation should be understood from inline comments, but the gist of this function is that it's able to
 * return something like "Just now", "45 minutes ago", "In 3 days", or "Mar 14, 1996".
 */
internal fun prettyRelativeTimeDifference(reference: Date, date: Date): String {
  val isAfter = date.getTime() >= reference.getTime()
  val secondsDiff = abs(reference.getTime() - date.getTime())
  val result = when {
    secondsDiff < 30_000 -> {
      // If less than 30s, "Just now".
      return "Just now"
    }
    secondsDiff < 90_000 -> {
      // If less than 1m30s, "1 minute".
      "1 minute"
    }
    secondsDiff < 3_570_000 -> {
      // If less than 59m30s, "X minutes".
      "${round(secondsDiff / 60_000)} minutes"
    }
    secondsDiff < 5_400_000 -> {
      // If less than 1h30m, "1 hour".
      "1 hour"
    }
    secondsDiff < 23_400_000 -> {
      // If less than 6h30m, "X hours".
      "${round(secondsDiff / 3_600_000)} hours"
    }
    secondsDiff < 84_600_000 -> {
      // If less than 23h30m...
      if (date.getDate() == reference.getDate()) {
        // If the date is the same, "X hours".
        "${round(secondsDiff / 3_600_000)} hours"
      } else {
        // If the date is not the same, "Yesterday" or "Tomorrow".
        return if (isAfter) "Tomorrow" else "Yesterday"
      }
    }
    secondsDiff < 129_600_000 -> {
      // If less than 1d12h, "Yesterday" or "Tomorrow".
      return if (isAfter) "Tomorrow" else "Yesterday"
    }
    else -> {
      // Otherwise format the date.
      @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
      val options = js {} as Date.LocaleOptions
      options.day = "numeric"
      options.month = "short"
      // If the year is not the same, include the year.
      if (date.getFullYear() != reference.getFullYear()) {
        options.year = "numeric"
      }
      return date.toLocaleDateString(options = options)
    }
  }
  return if (isAfter) "In $result" else "$result ago"
}
