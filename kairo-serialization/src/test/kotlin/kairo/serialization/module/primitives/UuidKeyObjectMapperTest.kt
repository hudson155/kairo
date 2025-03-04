package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
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

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8") to "value")))
      .shouldBe("{\"values\":{\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

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
