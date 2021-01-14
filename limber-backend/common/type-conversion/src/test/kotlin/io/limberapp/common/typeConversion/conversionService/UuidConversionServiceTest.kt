package io.limberapp.common.typeConversion.conversionService

import io.limberapp.common.util.uuid.base64Encode
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class UuidConversionServiceTest {
  @Test
  fun isValid() {
    assertFalse(UuidConversionService.isValid(""))
    repeat(10) { assertTrue(UuidConversionService.isValid(UUID.randomUUID().toString())) }
    assertTrue(UuidConversionService.isValid("cc45dfe4-95cb-4900-9c63-da8738a1a362"))
    assertTrue(UuidConversionService.isValid("cc45dfe495cb49009c63da8738a1a362"))
    assertTrue(UuidConversionService.isValid("CC45DFE4-95CB-4900-9C63-da8738a1a362"))
    assertFalse(UuidConversionService.isValid("gc45dfe4-95cb-4900-9c63-da8738a1a362"))
  }

  @Test
  fun parseString() {
    assertFails { UuidConversionService.parseString("") }
    List(10) { UUID.randomUUID() }.forEach { uuid ->
      assertEquals(uuid, UuidConversionService.parseString(uuid.toString()))
      assertEquals(uuid, UuidConversionService.parseString(uuid.base64Encode()))
    }
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidConversionService.parseString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidConversionService.parseString("feb6b42569a64a6ab8668f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidConversionService.parseString("FEB6B425-69A6-4A6A-B866-8F3E856E54D6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        UuidConversionService.parseString("/ra0JWmmSmq4Zo8+hW5U1g=="))
    assertFails { UuidConversionService.parseString("invalid") }
  }

  @Test
  fun writeString() {
    List(10) { UUID.randomUUID() }.forEach { uuid ->
      assertEquals(uuid.toString(), UuidConversionService.writeString(uuid))
    }
    assertEquals("feb6b425-69a6-4a6a-b866-8f3e856e54d6",
        UuidConversionService.writeString(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6")))
  }
}
