package io.limberapp.common.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.common.typeConversion.typeConverter.UuidTypeConverter
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

internal class ObjectMapperTest {
  @Test
  fun default() {
    val objectMapper = LimberObjectMapper()

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42}"),
    )
    assertFails {
      objectMapper.readValue("{\"str\":\"strVal\",\"int\":42,\"new\":true}")
    }
    assertEquals(
        expected = "{\"str\":\"strVal\",\"int\":42}",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }

  @Test
  fun yamlFactory() {
    val objectMapper = LimberObjectMapper(factory = LimberObjectMapper.Factory.YAML)

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("str: 'strVal'\nint: 42"),
    )
    assertFails {
      objectMapper.readValue("str: 'strVal'\nint: 42\nnew: true")
    }
    assertEquals(
        expected = "---\nstr: \"strVal\"\nint: 42\n",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }

  @Test
  fun typeConverter() {
    val objectMapper = LimberObjectMapper(typeConverters = listOf(UuidTypeConverter))

    assertEquals(
        expected = TestClass2(UUID.fromString("a0709f3f-bb5e-4dea-bcd0-c88664de3497"), 42),
        actual = objectMapper.readValue("{\"guid\":\"oHCfP7teTeq80MiGZN40lw==\",\"int\":42}"),
    )
    assertFails {
      objectMapper.readValue("{\"guid\":\"oHCfP7teTeq80MiGZN40lw==\",\"int\":42,\"new\":true}")
    }
    assertEquals(
        expected = "{\"guid\":\"a0709f3f-bb5e-4dea-bcd0-c88664de3497\",\"int\":42}",
        actual = objectMapper.writeValueAsString(
            TestClass2(UUID.fromString("a0709f3f-bb5e-4dea-bcd0-c88664de3497"), 42),
        ),
    )
  }

  @Test
  fun allowUnknownProperties() {
    val objectMapper = LimberObjectMapper(allowUnknownProperties = true)

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42}"),
    )
    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42,\"new\":true}"),
    )
    assertEquals(
        expected = "{\"str\":\"strVal\",\"int\":42}",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }

  @Test
  fun prettyPrint() {
    val objectMapper = LimberObjectMapper(prettyPrint = true)

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42}"),
    )
    assertFails {
      objectMapper.readValue("{\"str\":\"strVal\",\"int\":42,\"new\":true}")
    }
    assertEquals(
        expected = "{\n  \"int\": 42,\n  \"str\": \"strVal\"\n}",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }
}
