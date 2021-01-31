package io.limberapp.common.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.readValue
import io.limberapp.common.typeConversion.typeConverter.UuidTypeConverter
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

internal class ObjectMapperTest {
  @Test
  fun `default mapper - happy path`() {
    val objectMapper = LimberObjectMapper()

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42}"),
    )
    assertEquals(
        expected = "{\"str\":\"strVal\",\"int\":42}",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }

  @Test
  fun `default mapper - additional property`() {
    val objectMapper = LimberObjectMapper()

    assertFailsWith<UnrecognizedPropertyException> {
      objectMapper.readValue<TestClass1>("{\"str\":\"strVal\",\"int\":42,\"new\":true}")
    }
  }

  @Test
  fun `default mapper - missing property`() {
    val objectMapper = LimberObjectMapper()

    assertFailsWith<MissingKotlinParameterException> {
      objectMapper.readValue<TestClass1>("{\"int\":\"42\"}")
    }
    assertFailsWith<MismatchedInputException> {
      objectMapper.readValue<TestClass1>("{\"str\":\"strVal\"}")
    }
  }

  @Test
  fun `mapper with YAML factory - happy path`() {
    val objectMapper = LimberObjectMapper(factory = LimberObjectMapper.Factory.YAML)

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("str: 'strVal'\nint: 42"),
    )
    assertEquals(
        expected = "---\nstr: \"strVal\"\nint: 42\n",
        actual = objectMapper.writeValueAsString(TestClass1("strVal", 42)),
    )
  }

  @Test
  fun `mapper with type converter - happy path`() {
    val objectMapper = LimberObjectMapper(typeConverters = setOf(UuidTypeConverter))

    assertEquals(
        expected = TestClass2(UUID.fromString("a0709f3f-bb5e-4dea-bcd0-c88664de3497"), 42),
        actual = objectMapper.readValue("{\"guid\":\"oHCfP7teTeq80MiGZN40lw==\",\"int\":42}"),
    )
    assertEquals(
        expected = "{\"guid\":\"a0709f3f-bb5e-4dea-bcd0-c88664de3497\",\"int\":42}",
        actual = objectMapper.writeValueAsString(
            TestClass2(UUID.fromString("a0709f3f-bb5e-4dea-bcd0-c88664de3497"), 42),
        ),
    )
  }

  @Test
  fun `mapper that allows unknown properties - happy path`() {
    val objectMapper = LimberObjectMapper(allowUnknownProperties = true)

    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42}"),
    )
    assertEquals(
        expected = TestClass1("strVal", 42),
        actual = objectMapper.readValue("{\"str\":\"strVal\",\"int\":42,\"new\":true}"),
    )
  }

  @Test
  fun `mapper with pretty print enabled - happy path`() {
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
