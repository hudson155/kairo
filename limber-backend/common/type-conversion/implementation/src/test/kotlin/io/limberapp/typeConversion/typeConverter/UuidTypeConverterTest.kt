package io.limberapp.typeConversion.typeConverter

import io.limberapp.typeConversion.TypeConverter
import io.limberapp.util.uuid.base64Encode
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(TypeConverter.Unsafe::class)
internal class UuidTypeConverterTest {
  @Test
  fun canConvert() {
    assertFalse(UuidTypeConverter.canConvert(String::class.java))
    assertTrue(UuidTypeConverter.canConvert(UUID::class.java))
  }

  @Test
  fun isValid() {
    assertFalse(UuidTypeConverter.isValid(""))
    repeat(10) { assertTrue(UuidTypeConverter.isValid(UUID.randomUUID().toString())) }
    assertTrue(UuidTypeConverter.isValid("cc45dfe4-95cb-4900-9c63-da8738a1a362"))
    assertTrue(UuidTypeConverter.isValid("cc45dfe495cb49009c63da8738a1a362"))
    assertTrue(UuidTypeConverter.isValid("CC45DFE4-95CB-4900-9C63-da8738a1a362"))
    assertFalse(UuidTypeConverter.isValid("gc45dfe4-95cb-4900-9c63-da8738a1a362"))
  }

  @Test
  fun parseString() {
    assertFails { UuidTypeConverter.parseString("") }
    List(10) { UUID.randomUUID() }.forEach { uuid ->
      assertEquals(uuid, UuidTypeConverter.parseString(uuid.toString()))
      assertEquals(uuid, UuidTypeConverter.parseString(uuid.base64Encode()))
    }
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidTypeConverter.parseString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidTypeConverter.parseString("feb6b42569a64a6ab8668f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidTypeConverter.parseString("FEB6B425-69A6-4A6A-B866-8F3E856E54D6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidTypeConverter.parseString("/ra0JWmmSmq4Zo8+hW5U1g=="))
    assertFails { UuidTypeConverter.parseString("invalid") }
  }

  @Test
  fun writeStringUnsafe() {
    assertFailsWith<ClassCastException> { UuidTypeConverter.writeStringUnsafe("") }

    List(10) { UUID.randomUUID() }.forEach { uuid ->
      assertEquals(uuid.toString(), UuidTypeConverter.writeStringUnsafe(uuid))
    }
    assertEquals("feb6b425-69a6-4a6a-b866-8f3e856e54d6",
        UuidTypeConverter.writeStringUnsafe(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6")))
  }

  @Test
  fun writeString() {
    List(10) { UUID.randomUUID() }.forEach { uuid ->
      assertEquals(uuid.toString(), UuidTypeConverter.writeString(uuid))
    }
    assertEquals("feb6b425-69a6-4a6a-b866-8f3e856e54d6",
        UuidTypeConverter.writeString(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6")))
  }
}
