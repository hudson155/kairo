package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to local date map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to local date map keys.
 */
internal class LocalDateKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<LocalDate?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(mapOf(LocalDate.parse("2023-11-13") to "value")))
      .shouldBe("{\"values\":{\"2023-11-13\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.writeValueAsString(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"2023-11-13\": \"value\" } }")
      .shouldBe(MyClass(mapOf(LocalDate.parse("2023-11-13") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
