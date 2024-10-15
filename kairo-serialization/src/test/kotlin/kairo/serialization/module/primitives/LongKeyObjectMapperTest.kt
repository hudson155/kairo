package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to long map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to long map keys.
 */
internal class LongKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Long?, String>,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"42\": \"value\" } }")
      .shouldBe(MyClass(mapOf(42L to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: 42 } }")
    }
  }
}
