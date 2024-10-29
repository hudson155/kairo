package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to double serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to doubles.
 */
internal class DoubleDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: Double,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, positive`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(1.23)).shouldBe("{\"value\":1.23}")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(-1.23)).shouldBe("{\"value\":-1.23}")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": 1.23 }").shouldBe(MyClass(1.23))
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": -1.23 }").shouldBe(MyClass(-1.23))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, wrong type, int`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42.0))
  }

  @Test
  fun `deserialize, wrong type, string`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"1.23\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
