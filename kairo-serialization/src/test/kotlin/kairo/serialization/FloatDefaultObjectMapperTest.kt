package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to float serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to floats.
 */
internal class FloatDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: Float,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, positive`() {
    mapper.writeValueAsString(MyClass(1.23F)).shouldBe("{\"value\":1.23}")
  }

  @Test
  fun `serialize, negative`() {
    mapper.writeValueAsString(MyClass(-1.23F)).shouldBe("{\"value\":-1.23}")
  }

  @Test
  fun `deserialize, positive`() {
    mapper.readValue<MyClass>("{ \"value\": 1.23 }").shouldBe(MyClass(1.23F))
  }

  @Test
  fun `deserialize, negative`() {
    mapper.readValue<MyClass>("{ \"value\": -1.23 }").shouldBe(MyClass(-1.23F))
  }

  @Test
  fun `deserialize, null`() {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`() {
    serializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, wrong type, int`() {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42.0F))
  }

  @Test
  fun `deserialize, wrong type, string`() {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"1.23\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`() {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
