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
 * This test is intended to test behaviour strictly related to string map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to string map keys.
 */
internal class StringKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<String?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf("key" to "value")))
      .shouldBe("{\"values\":{\"key\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"key\": \"value\" } }")
      .shouldBe(MyClass(mapOf("key" to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
