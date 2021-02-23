package io.limberapp.typeConversion.typeConverter

import io.limberapp.typeConversion.TypeConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(TypeConverter.Unsafe::class)
internal class RegexTypeConverterTest {
  @Test
  fun canConvert() {
    assertFalse(RegexTypeConverter.canConvert(String::class.java))
    assertTrue(RegexTypeConverter.canConvert(Regex::class.java))
  }

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
  fun writeStringUnsafe() {
    assertFailsWith<ClassCastException> { RegexTypeConverter.writeStringUnsafe("") }

    assertEquals("", RegexTypeConverter.writeStringUnsafe(Regex("")))
    assertEquals("abc", RegexTypeConverter.writeStringUnsafe(Regex("abc")))
    assertEquals("^abc$", RegexTypeConverter.writeStringUnsafe(Regex("^abc$")))
    @Suppress("RegExpUnexpectedAnchor")
    assertEquals("\$a^", RegexTypeConverter.writeStringUnsafe(Regex("\$a^")))
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
