package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to boolean map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to boolean map keys.
 */
internal class BooleanKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Boolean?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, false`(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(false to "value")))
      .shouldBe("{\"values\":{\"false\":\"value\"}}")
  }

  @Test
  fun `serialize, true`(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(true to "value")))
      .shouldBe("{\"values\":{\"true\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, false`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"false\": \"value\" } }")
      .shouldBe(MyClass(mapOf(false to "value")))
  }

  @Test
  fun `deserialize, true`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"true\": \"value\" } }")
      .shouldBe(MyClass(mapOf(true to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
