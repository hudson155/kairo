package io.limberapp.permissions.limber

import io.limberapp.typeConversion.typeConverter.LimberPermissionsTypeConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

internal class LimberPermissionsTypeConverterTest {
  private val typeConverter: LimberPermissionsTypeConverter =
      LimberPermissionsTypeConverter

  @Test
  fun isValid() {
    assertNull(typeConverter.isValid(""))
  }

  @Test
  fun parseString() {
    assertEquals("00",
        typeConverter.parseString("0.").asBitString())
    assertEquals("10",
        typeConverter.parseString("1.8").asBitString())
    assertFails { typeConverter.parseString("1.G") }
    assertFails { typeConverter.parseString("T.1.8") }
  }

  @Test
  fun writeString() {
    assertEquals("2.0", typeConverter.writeString(LimberPermissions.none()))
    assertEquals("2.8", typeConverter.writeString(LimberPermissions.fromBitString("1")))
  }
}
