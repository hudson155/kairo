package io.limberapp.common.typeConversion.conversionService

import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class TimeZoneConversionServiceTest {
  @Test
  fun parseString() {
    assertFails { TimeZoneConversionService.parseString("") }

    assertEquals(ZoneOffset.UTC, TimeZoneConversionService.parseString("Z"))
    assertFails { TimeZoneConversionService.parseString("A") }

    listOf("", "GMT", "UTC", "UT").forEach { prefix ->
      assertEquals(ZoneOffset.ofHours(-7),
          TimeZoneConversionService.parseString("$prefix-7"))
      assertEquals(ZoneOffset.ofHours(11),
          TimeZoneConversionService.parseString("$prefix+11"))
      assertFails { TimeZoneConversionService.parseString("${prefix}11") }
      assertEquals(ZoneOffset.ofHoursMinutes(-3, -30),
          TimeZoneConversionService.parseString("$prefix-03:30"))
      assertFails { TimeZoneConversionService.parseString("$prefix-3:30") }
      assertEquals(ZoneOffset.ofHoursMinutesSeconds(12, 34, 56),
          TimeZoneConversionService.parseString("$prefix+123456"))
      assertFails { TimeZoneConversionService.parseString("${prefix}123456") }
    }

    assertEquals(ZoneOffset.UTC, TimeZoneConversionService.parseString("GMT"))
    assertEquals(ZoneOffset.UTC, TimeZoneConversionService.parseString("UTC"))
    assertEquals(ZoneOffset.UTC, TimeZoneConversionService.parseString("UT"))

    assertEquals(ZoneId.of("America/New_York"),
        TimeZoneConversionService.parseString("America/New_York"))
    assertEquals(ZoneId.of("America/Edmonton"),
        TimeZoneConversionService.parseString("America/Edmonton"))
    assertFails { TimeZoneConversionService.parseString("America/Calgary") }

    assertFails { TimeZoneConversionService.parseString("!@#$%^&*()") }
  }

  @Test
  fun writeString() {
    assertEquals("Z", TimeZoneConversionService.writeString(ZoneOffset.UTC))

    assertEquals("-07:00",
        TimeZoneConversionService.writeString(ZoneOffset.ofHours(-7)))
    assertEquals("-07:00",
        TimeZoneConversionService.writeString(ZoneOffset.ofHoursMinutes(-7, 0)))
    assertEquals("+11:00",
        TimeZoneConversionService.writeString(ZoneOffset.ofHours(11)))
    assertEquals("-03:30",
        TimeZoneConversionService.writeString(ZoneOffset.ofHoursMinutes(-3, -30)))
    assertEquals("-03:30",
        TimeZoneConversionService.writeString(ZoneOffset.ofHoursMinutesSeconds(-3, -30, 0)))
    assertEquals("+12:34:56",
        TimeZoneConversionService.writeString(ZoneOffset.ofHoursMinutesSeconds(12, 34, 56)))

    assertEquals("America/New_York",
        TimeZoneConversionService.writeString(ZoneId.of("America/New_York")))
    assertEquals("America/Edmonton",
        TimeZoneConversionService.writeString(ZoneId.of("America/Edmonton")))
  }
}
