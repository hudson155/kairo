package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to strings.
 */
internal class StringDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: String,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, non-empty`() {
    mapper.writeValueAsString(MyClass("true")).shouldBe("{\"value\":\"true\"}")
  }

  @Test
  fun `serialize, empty`() {
    mapper.writeValueAsString(MyClass("")).shouldBe("{\"value\":\"\"}")
  }

  @Test
  fun `deserialize, non-empty`() {
    mapper.readValue<MyClass>("{ \"value\": \"true\" }").shouldBe(MyClass("true"))
  }

  @Test
  fun `deserialize, empty`() {
    mapper.readValue<MyClass>("{ \"value\": \"\" }").shouldBe(MyClass(""))
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
      mapper.readValue<MyClass>("{ \"value\": 1 }")
    }
  }

  @Test
  fun `deserialize, wrong type, float`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": 0.0 }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
