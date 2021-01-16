package io.limberapp.common.permissions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PermissionsSerializerTest {
  private val objectMapper: ObjectMapper = jacksonObjectMapper()
      .registerModule(SimpleModule()
          .addSerializer(Permissions::class.java, PermissionsSerializer()))

  @Test
  fun serialize() {
    assertEquals("\"2.0\"", objectMapper.writeValueAsString(LimberPermissions.none()))
    assertEquals("\"2.8\"", objectMapper.writeValueAsString(LimberPermissions.fromBitString("1")))
  }
}
