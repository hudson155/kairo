package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import java.time.Year
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to year-month map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to year-month map keys.
 */
internal class YearKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<Year?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(Year.parse("2023") to "value")))
      .shouldBe("{\"values\":{\"2023\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"values\": { \"2023\": \"value\" } }")
      .shouldBe(MyClass(mapOf(Year.parse("2023") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoRead<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
