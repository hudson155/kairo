package io.limberapp.common.typeConversion.conversionService

import kotlin.test.Test
import kotlin.test.assertEquals

internal class RegexConversionServiceTest {
  @Test
  fun parseString() {
    assertEquals(Regex("").toString(), RegexConversionService.parseString("").toString())
    assertEquals(Regex("abc").toString(), RegexConversionService.parseString("abc").toString())
    assertEquals(Regex("^abc$").toString(), RegexConversionService.parseString("^abc$").toString())
    @Suppress("RegExpUnexpectedAnchor")
    assertEquals(Regex("\$a^").toString(), RegexConversionService.parseString("\$a^").toString())
  }

  @Test
  fun writeString() {
    assertEquals("", RegexConversionService.writeString(Regex("")))
    assertEquals("abc", RegexConversionService.writeString(Regex("abc")))
    assertEquals("^abc$", RegexConversionService.writeString(Regex("^abc$")))
    @Suppress("RegExpUnexpectedAnchor")
    assertEquals("\$a^", RegexConversionService.writeString(Regex("\$a^")))
  }
}
