package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to long serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to longs.
 */
internal class LongDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: Long,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, positive`() {
    mapper.writeValueAsString(MyClass(42L)).shouldBe("{\"value\":42}")
  }

  @Test
  fun `serialize, negative`() {
    mapper.writeValueAsString(MyClass(-42L)).shouldBe("{\"value\":-42}")
  }

  @Test
  fun `deserialize, positive`() {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42L))
  }

  @Test
  fun `deserialize, negative`() {
    mapper.readValue<MyClass>("{ \"value\": -42 }").shouldBe(MyClass(-42L))
  }

  @Test
  fun `deserialize, null`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, wrong type, float`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1.23 }")
    }
  }

  @Test
  fun `deserialize, wrong type, string`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"42\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}