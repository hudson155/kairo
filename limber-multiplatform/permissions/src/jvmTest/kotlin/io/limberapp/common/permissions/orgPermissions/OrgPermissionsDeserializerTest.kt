package io.limberapp.common.permissions.orgPermissions

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class OrgPermissionsDeserializerTest {
  private val objectMapper = jacksonObjectMapper()
      .registerModule(SimpleModule()
          .addDeserializer(OrgPermissions::class.java, OrgPermissionsDeserializer()))

  @Test
  fun deserialize() {
    assertEquals("0000",
        objectMapper.readValue<OrgPermissions>("\"0.\"").asBitString())
    assertEquals("0110",
        objectMapper.readValue<OrgPermissions>("\"3.6\"").asBitString())
    assertFails { objectMapper.readValue<OrgPermissions>("\"3.G\"") }
    assertFails { objectMapper.readValue<OrgPermissions>("\"T.3.6\"") }
  }
}
