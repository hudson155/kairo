package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to boolean serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to booleans.
 */
internal class BooleanDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: Boolean,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, false`() {
    mapper.writeValueAsString(MyClass(false)).shouldBe("{\"value\":false}")
  }

  @Test
  fun `serialize, true`() {
    mapper.writeValueAsString(MyClass(true)).shouldBe("{\"value\":true}")
  }

  @Test
  fun `deserialize, false`() {
    mapper.readValue<MyClass>("{ \"value\": false }").shouldBe(MyClass(false))
  }

  @Test
  fun `deserialize, true`() {
    mapper.readValue<MyClass>("{ \"value\": true }").shouldBe(MyClass(true))
  }

  @Test
  fun `deserialize, null`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, wrong type, int`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": 0 }")
    }
  }

  @Test
  fun `deserialize, wrong type, float`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": 1.0 }")
    }
  }

  @Test
  fun `deserialize, wrong type, string`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": \"false\" }")
    }
  }
}
