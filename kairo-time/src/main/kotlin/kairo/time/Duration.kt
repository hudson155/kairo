package kairo.time

import java.time.Duration

@Suppress("CognitiveComplexMethod", "ComplexCondition", "CyclomaticComplexMethod", "LongMethod")
public fun Duration.kairoFormat(): String {
  val days = toDaysPart()
  val hours = toHoursPart()
  val minutes = toMinutesPart()
  val seconds = toSecondsPart()
  val milliseconds = toMillisPart()

  val daysPart = "$days ${if (days == 1L) "day" else "days"}"
  val hoursPart = "$hours ${if (hours == 1) "hour" else "hours"}"
  val minutesPart = "$minutes ${if (minutes == 1) "minute" else "minutes"}"
  val secondsPart = "$seconds ${if (seconds == 1) "second" else "seconds"}"
  val extendedSecondsPart = buildString {
    append(seconds)
    append('.')
    append(milliseconds.toString().padStart(3, '0'))
    append(' ')
    append(if (seconds == 1 && milliseconds == 0) "second" else "seconds")
  }

  if (days > 0L) {
    if (hours == 0 && minutes == 0 && seconds == 0 && milliseconds == 0) return daysPart
    return "$daysPart $hoursPart"
  }
  if (hours > 0) {
    if (minutes == 0 && seconds == 0 && milliseconds == 0) return hoursPart
    return "$hoursPart $minutesPart"
  }
  if (minutes > 0) {
    if (seconds == 0 && milliseconds == 0) return minutesPart
    return "$minutesPart $secondsPart"
  }
  return extendedSecondsPart
}
