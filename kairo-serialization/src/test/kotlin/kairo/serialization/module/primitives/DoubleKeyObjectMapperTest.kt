package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
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
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(1.23 to "value")))
      .shouldBe("{\"values\":{\"1.23\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"values\": { \"1.23\": \"value\" } }")
      .shouldBe(MyClass(mapOf(1.23 to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
