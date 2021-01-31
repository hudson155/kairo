package io.limberapp.common.typeConversion.typeConverter

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class RegexTypeConverterTest {
  @Test
  fun isValid() {
    assertNull(RegexTypeConverter.isValid(""))
  }

  @Test
  fun parseString() {
    assertEquals(Regex("").toString(), RegexTypeConverter.parseString("").toString())
    assertEquals(Regex("abc").toString(), RegexTypeConverter.parseString("abc").toString())
    assertEquals(Regex("^abc$").toString(), RegexTypeConverter.parseString("^abc$").toString())
    @Suppress("RegExpUnexpectedAnchor")
    assertEquals(Regex("\$a^").toString(), RegexTypeConverter.parseString("\$a^").toString())
  }

  @Test
  fun writeString() {
    assertEquals("", RegexTypeConverter.writeString(Regex("")))
    assertEquals("abc", RegexTypeConverter.writeString(Regex("abc")))
    assertEquals("^abc$", RegexTypeConverter.writeString(Regex("^abc$")))
    @Suppress("RegExpUnexpectedAnchor")
    assertEquals("\$a^", RegexTypeConverter.writeString(Regex("\$a^")))
  }
}
