package io.limberapp.common.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class ObjectMapperTest {
  @Test
  fun serialize() {
    val testObject = TestClass("strVal", 42)
    assertEquals("{\"str\":\"strVal\",\"int\":42}",
        limberObjectMapper().writeValueAsString(testObject))
    assertEquals("{\n  \"int\": 42,\n  \"str\": \"strVal\"\n}",
        limberObjectMapper(prettyPrint = true).writeValueAsString(testObject))
  }

  @Test
  fun deserialize() {
    val string = "{\"str\":\"strVal\",\"int\":42,\"new\":true}"
    assertEquals(TestClass("strVal", 42),
        limberObjectMapper(allowUnknownProperties = true).readValue(string))
    assertFails { limberObjectMapper().readValue(string) }
  }
}
