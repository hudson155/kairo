package io.limberapp.permissions.org

import io.limberapp.typeConversion.typeConverter.OrgPermissionsTypeConverter
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

internal class OrgPermissionsTypeConverterTest {
  private val typeConverter: OrgPermissionsTypeConverter =
      OrgPermissionsTypeConverter

  @Test
  fun isValid() {
    assertNull(typeConverter.isValid(""))
  }

  @Test
  fun deserialize() {
    assertEquals("0000",
        typeConverter.parseString("0.").asBitString())
    assertEquals("0110",
        typeConverter.parseString("3.6").asBitString())
    assertFails { typeConverter.parseString("3.G") }
    assertFails { typeConverter.parseString("T.3.6") }
  }

  @Test
  fun writeString() {
    assertEquals("4.0", typeConverter.writeString(OrgPermissions.none()))
    assertEquals("4.2", typeConverter.writeString(OrgPermissions.fromBitString("001")))
  }
}
