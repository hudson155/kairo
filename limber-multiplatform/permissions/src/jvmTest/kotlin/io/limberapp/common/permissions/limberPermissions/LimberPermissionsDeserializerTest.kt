package io.limberapp.common.permissions.limberPermissions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class LimberPermissionsDeserializerTest {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()
      .registerModule(SimpleModule()
          .addDeserializer(LimberPermissions::class.java, LimberPermissionsDeserializer()))

  @Test
  fun deserialize() {
    assertEquals("00",
        objectMapper.readValue<LimberPermissions>("\"0.\"").asBitString())
    assertEquals("10",
        objectMapper.readValue<LimberPermissions>("\"1.8\"").asBitString())
    assertFails { objectMapper.readValue<LimberPermissions>("\"1.G\"") }
    assertFails { objectMapper.readValue<LimberPermissions>("\"T.1.8\"") }
  }
}
