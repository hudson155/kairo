package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to int map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to int map keys.
 */
internal class IntKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Int?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(42 to "value")))
      .shouldBe("{\"values\":{\"42\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"42\": \"value\" } }")
      .shouldBe(MyClass(mapOf(42 to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: 42 } }")
    }
  }
}
