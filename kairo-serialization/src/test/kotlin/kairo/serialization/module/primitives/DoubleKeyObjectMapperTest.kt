package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to double map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to double map keys.
 */
internal class DoubleKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Double?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(mapOf(1.23 to "value")))
      .shouldBe("{\"values\":{\"1.23\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.writeValueAsString(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"1.23\": \"value\" } }")
      .shouldBe(MyClass(mapOf(1.23 to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
