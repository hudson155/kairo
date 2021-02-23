package io.limberapp.server.feature

import io.ktor.util.ConversionService
import io.limberapp.typeConversion.typeConverter.UuidTypeConverter
import io.limberapp.util.uuid.base64Encode
import org.junit.jupiter.api.Test
import java.lang.reflect.Type
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

internal class DataConversionTest {
  private val conversionService: ConversionService = conversionService(UuidTypeConverter)

  @Test
  fun `from values`() {
    assertNull(fromValues())
    assertFails { fromValues("") }
    repeat(10) {
      val guid = UUID.randomUUID()
      assertEquals(guid, fromValues(guid.toString()))
      assertEquals(guid, fromValues(guid.base64Encode()))
    }
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        fromValues("feb6b425-69a6-4a6a-b866-8f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        fromValues("feb6b42569a64a6ab8668f3e856e54d6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        fromValues("FEB6B425-69A6-4A6A-B866-8F3E856E54D6"))
    assertEquals(UUID.fromString("feb6b425-69a6-4a6a-b866-8f3e856e54d6"),
        fromValues("/ra0JWmmSmq4Zo8+hW5U1g=="))
    assertFails { fromValues("invalid") }
    assertFails {
      fromValues("feb6b425-69a6-4a6a-b866-8f3e856e54d6", "feb6b425-69a6-4a6a-b866-8f3e856e54d6")
    }
    assertFails {
      fromValues("feb6b425-69a6-4a6a-b866-8f3e856e54d6", type = String::class.javaObjectType)
    }
  }

  @Test
  fun `to values`() {
    assertEquals(emptyList(), toValues(null))
    assertFails { toValues("") }
    repeat(10) {
      val guid = UUID.randomUUID()
      assertEquals(listOf(guid.toString()), toValues(guid))
    }
    assertFails { toValues(UUID.randomUUID().toString()) }
  }

  private fun fromValues(vararg values: String, type: Type = UUID::class.javaObjectType): Any? =
      conversionService.fromValues(values.toList(), type)

  private fun toValues(value: Any?): List<String> =
      conversionService.toValues(value)
}
