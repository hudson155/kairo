package io.limberapp.permissions.feature

import io.limberapp.typeConversion.typeConverter.FeaturePermissionsTypeConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

internal class FeaturePermissionsTypeConverterTest {
  private val typeConverter: FeaturePermissionsTypeConverter =
      FeaturePermissionsTypeConverter(mapOf('T' to TestFeaturePermissions))

  @Test
  fun isValid() {
    assertNull(typeConverter.isValid(""))
  }

  @Test
  fun parseString() {
    assertEquals("T00",
        typeConverter.parseString("T.0.").asBitString())
    assertEquals("T10",
        typeConverter.parseString("T.1.8").asBitString())
    assertFails { typeConverter.parseString("T.1.G") }
    assertFails { typeConverter.parseString(".1.8") }
    assertFails { typeConverter.parseString("1.8") }
    assertFails { typeConverter.parseString("TT.1.8") }
    assertEquals("U10", typeConverter.parseString("U.2.8").asBitString())
  }

  @Test
  fun writeString() {
    assertEquals("T.2.0", typeConverter.writeString(TestFeaturePermissions.none()))
    assertEquals("T.2.8", typeConverter.writeString(TestFeaturePermissions.fromBitString("1")))
  }
}
