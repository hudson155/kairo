package io.limberapp.common.permissions.featurePermissions

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class FeaturePermissionsDeserializerTest {
  private val objectMapper = jacksonObjectMapper()
      .registerModule(SimpleModule()
          .addDeserializer(FeaturePermissions::class.java, TestFeaturePermissionsDeserializer()))

  @Test
  fun deserialize() {
    assertEquals("T00",
        objectMapper.readValue<FeaturePermissions<*>>("\"T.0.\"").asBitString())
    assertEquals("T10",
        objectMapper.readValue<FeaturePermissions<*>>("\"T.1.8\"").asBitString())
    assertFails { objectMapper.readValue<FeaturePermissions<*>>("\"T.1.G\"") }
    assertFails { objectMapper.readValue<FeaturePermissions<*>>("\"1.8\"") }
    assertFails { objectMapper.readValue<FeaturePermissions<*>>("\"U.1.8\"") }
  }
}
