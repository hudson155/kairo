@file:Suppress("InvalidPackageDeclaration") // Detekt false positive.

package io.limberapp.common.util.date

import kotlin.js.Date
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("StringLiteralDuplication")
internal class DateTest {
  private val startOfYear = Date(2020, 0, 1)
  private val endOfYear = Date(2020, 11, 31, 23, 59, 59)

  @Test
  fun justNow() {
    val ms15Seconds = 15_000.toDouble()
    testDiff("Just now", startOfYear, -ms15Seconds)
    testDiff("Just now", endOfYear, -ms15Seconds)
    testDiff("Just now", startOfYear, ms15Seconds)
    testDiff("Just now", endOfYear, ms15Seconds)
    val ms0Seconds = 0.toDouble()
    testDiff("Just now", startOfYear, ms0Seconds)
    testDiff("Just now", endOfYear, ms0Seconds)
  }

  @Test
  fun oneMinute() {
    val ms45Seconds = 45_000.toDouble()
    testDiff("1 minute ago", startOfYear, -ms45Seconds)
    testDiff("1 minute ago", endOfYear, -ms45Seconds)
    testDiff("In 1 minute", startOfYear, ms45Seconds)
    testDiff("In 1 minute", endOfYear, ms45Seconds)
    val ms1Minute15Seconds = 75_000.toDouble()
    testDiff("1 minute ago", startOfYear, -ms1Minute15Seconds)
    testDiff("1 minute ago", endOfYear, -ms1Minute15Seconds)
    testDiff("In 1 minute", startOfYear, ms1Minute15Seconds)
    testDiff("In 1 minute", endOfYear, ms1Minute15Seconds)
  }

  @Test
  fun multipleMinutes() {
    val ms1Minute45Seconds = 105_000.toDouble()
    testDiff("2 minutes ago", startOfYear, -ms1Minute45Seconds)
    testDiff("2 minutes ago", endOfYear, -ms1Minute45Seconds)
    testDiff("In 2 minutes", startOfYear, ms1Minute45Seconds)
    testDiff("In 2 minutes", endOfYear, ms1Minute45Seconds)
    val ms2Minutes15Seconds = 135_000.toDouble()
    testDiff("2 minutes ago", startOfYear, -ms2Minutes15Seconds)
    testDiff("2 minutes ago", endOfYear, -ms2Minutes15Seconds)
    testDiff("In 2 minutes", startOfYear, ms2Minutes15Seconds)
    testDiff("In 2 minutes", endOfYear, ms2Minutes15Seconds)
    val ms2Minutes45Seconds = 165_000.toDouble()
    testDiff("3 minutes ago", startOfYear, -ms2Minutes45Seconds)
    testDiff("3 minutes ago", endOfYear, -ms2Minutes45Seconds)
    testDiff("In 3 minutes", startOfYear, ms2Minutes45Seconds)
    testDiff("In 3 minutes", endOfYear, ms2Minutes45Seconds)
    val ms58Minutes15Seconds = 3_495_000.toDouble()
    testDiff("58 minutes ago", startOfYear, -ms58Minutes15Seconds)
    testDiff("58 minutes ago", endOfYear, -ms58Minutes15Seconds)
    testDiff("In 58 minutes", startOfYear, ms58Minutes15Seconds)
    testDiff("In 58 minutes", endOfYear, ms58Minutes15Seconds)
    val ms58Minutes45Seconds = 3_555_000.toDouble()
    testDiff("59 minutes ago", startOfYear, -ms58Minutes45Seconds)
    testDiff("59 minutes ago", endOfYear, -ms58Minutes45Seconds)
    testDiff("In 59 minutes", startOfYear, ms58Minutes45Seconds)
    testDiff("In 59 minutes", endOfYear, ms58Minutes45Seconds)
    val ms59Minutes15Seconds = 3_555_000.toDouble()
    testDiff("59 minutes ago", startOfYear, -ms59Minutes15Seconds)
    testDiff("59 minutes ago", endOfYear, -ms59Minutes15Seconds)
    testDiff("In 59 minutes", startOfYear, ms59Minutes15Seconds)
    testDiff("In 59 minutes", endOfYear, ms59Minutes15Seconds)
  }

  @Test
  fun oneHour() {
    val ms59Minutes45Seconds = 3_585_000.toDouble()
    testDiff("1 hour ago", startOfYear, -ms59Minutes45Seconds)
    testDiff("1 hour ago", endOfYear, -ms59Minutes45Seconds)
    testDiff("In 1 hour", startOfYear, ms59Minutes45Seconds)
    testDiff("In 1 hour", endOfYear, ms59Minutes45Seconds)
    val ms1Hour15Minutes = 4_500_000.toDouble()
    testDiff("1 hour ago", startOfYear, -ms1Hour15Minutes)
    testDiff("1 hour ago", endOfYear, -ms1Hour15Minutes)
    testDiff("In 1 hour", startOfYear, ms1Hour15Minutes)
    testDiff("In 1 hour", endOfYear, ms1Hour15Minutes)
  }

  @Test
  fun multipleHours() {
    val ms1Hour45Minutes = 6_300_000.toDouble()
    testDiff("2 hours ago", startOfYear, -ms1Hour45Minutes)
    testDiff("2 hours ago", endOfYear, -ms1Hour45Minutes)
    testDiff("In 2 hours", startOfYear, ms1Hour45Minutes)
    testDiff("In 2 hours", endOfYear, ms1Hour45Minutes)
    val ms2Hours15Minutes = 8_100_000.toDouble()
    testDiff("2 hours ago", startOfYear, -ms2Hours15Minutes)
    testDiff("2 hours ago", endOfYear, -ms2Hours15Minutes)
    testDiff("In 2 hours", startOfYear, ms2Hours15Minutes)
    testDiff("In 2 hours", endOfYear, ms2Hours15Minutes)
    val ms2Hours45Minutes = 9_900_000.toDouble()
    testDiff("3 hours ago", startOfYear, -ms2Hours45Minutes)
    testDiff("3 hours ago", endOfYear, -ms2Hours45Minutes)
    testDiff("In 3 hours", startOfYear, ms2Hours45Minutes)
    testDiff("In 3 hours", endOfYear, ms2Hours45Minutes)
    val ms22Hours15Minutes = 80_100_000.toDouble()
    testDiff("Yesterday", startOfYear, -ms22Hours15Minutes)
    testDiff("22 hours ago", endOfYear, -ms22Hours15Minutes)
    testDiff("In 22 hours", startOfYear, ms22Hours15Minutes)
    testDiff("Tomorrow", endOfYear, ms22Hours15Minutes)
    val ms22Hours45Minutes = 81_900_000.toDouble()
    testDiff("Yesterday", startOfYear, -ms22Hours45Minutes)
    testDiff("23 hours ago", endOfYear, -ms22Hours45Minutes)
    testDiff("In 23 hours", startOfYear, ms22Hours45Minutes)
    testDiff("Tomorrow", endOfYear, ms22Hours45Minutes)
    val ms23Hours15Minutes = 83_700_000.toDouble()
    testDiff("Yesterday", startOfYear, -ms23Hours15Minutes)
    testDiff("23 hours ago", endOfYear, -ms23Hours15Minutes)
    testDiff("In 23 hours", startOfYear, ms23Hours15Minutes)
    testDiff("Tomorrow", endOfYear, ms23Hours15Minutes)
  }

  @Test
  fun oneDay() {
    val ms23Minutes45Minutes = 85_500_000.toDouble()
    testDiff("Yesterday", startOfYear, -ms23Minutes45Minutes)
    testDiff("Yesterday", endOfYear, -ms23Minutes45Minutes)
    testDiff("Tomorrow", startOfYear, ms23Minutes45Minutes)
    testDiff("Tomorrow", endOfYear, ms23Minutes45Minutes)
    val ms1Day6Hours = 108_000_000.toDouble()
    testDiff("Yesterday", startOfYear, -ms1Day6Hours)
    testDiff("Yesterday", endOfYear, -ms1Day6Hours)
    testDiff("Tomorrow", startOfYear, ms1Day6Hours)
    testDiff("Tomorrow", endOfYear, ms1Day6Hours)
  }

  @Test
  fun multipleDays() {
    val ms1Day18Hours = 151_200_000.toDouble()
    testDiff("Dec 30, 2019", startOfYear, -ms1Day18Hours)
    testDiff("Dec 30", endOfYear, -ms1Day18Hours)
    testDiff("Jan 2", startOfYear, ms1Day18Hours)
    testDiff("Jan 2, 2021", endOfYear, ms1Day18Hours)
    val ms2Days6Hours = 194_400_000.toDouble()
    testDiff("Dec 29, 2019", startOfYear, -ms2Days6Hours)
    testDiff("Dec 29", endOfYear, -ms2Days6Hours)
    testDiff("Jan 3", startOfYear, ms2Days6Hours)
    testDiff("Jan 3, 2021", endOfYear, ms2Days6Hours)
    val ms2Days18Hours = 237_600_000.toDouble()
    testDiff("Dec 29, 2019", startOfYear, -ms2Days18Hours)
    testDiff("Dec 29", endOfYear, -ms2Days18Hours)
    testDiff("Jan 3", startOfYear, ms2Days18Hours)
    testDiff("Jan 3, 2021", endOfYear, ms2Days18Hours)
  }

  private fun testDiff(expected: String, date: Date, diffMs: Double) {
    assertEquals(expected, prettyRelativeTimeDifference(date, Date(date.getTime() + diffMs)))
  }
}
