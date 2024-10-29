package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to instant map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to instant map keys.
 */
internal class InstantKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Instant?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(mapOf(Instant.parse("2023-11-13T19:44:32.123456789Z") to "value")))
      .shouldBe("{\"values\":{\"2023-11-13T19:44:32.123456789Z\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.writeValueAsString(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"2023-11-13T19:44:32.123456789Z\": \"value\" } }")
      .shouldBe(MyClass(mapOf(Instant.parse("2023-11-13T19:44:32.123456789Z") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
