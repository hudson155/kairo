package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.YearMonth
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to year-month map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to year-month map keys.
 */
internal class YearMonthKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<YearMonth?, String>,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun serialize(): Unit = runTest {
    mapper.kairoWrite(MyClass(mapOf(YearMonth.parse("2023-11") to "value")))
      .shouldBe("{\"values\":{\"2023-11\":\"value\"}}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(mapOf(null to "value")))
    }
  }

  @Test
  fun deserialize(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"2023-11\": \"value\" } }")
      .shouldBe(MyClass(mapOf(YearMonth.parse("2023-11") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
