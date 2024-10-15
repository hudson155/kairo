package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kairo.serialization.serializationShouldFail
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to UUID map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to UUID map keys.
 */
internal class UuidKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Uuid?, String>,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\": \"value\" } }")
      .shouldBe(MyClass(mapOf(Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
