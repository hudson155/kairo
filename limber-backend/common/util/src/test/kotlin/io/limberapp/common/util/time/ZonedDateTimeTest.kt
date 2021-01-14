package io.limberapp.common.util.time

import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class ZonedDateTimeTest {
  @Test
  fun inUTC() {
    val nowInEastern = ZonedDateTime.now(ZoneId.of("America/New_York"))
    val nowInUtc = nowInEastern.inUTC()
    assertEquals(ZoneId.of("America/New_York"), nowInEastern.zone)
    assertEquals(ZoneOffset.UTC, nowInUtc.zone)
    assertNotEquals(nowInEastern.hour, nowInUtc.hour)
    assertEquals(nowInEastern.toEpochSecond(), nowInUtc.toEpochSecond())
  }
}
