package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Year
import kairo.serialization.jsonMapper
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to year-month serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to year-months.
 */
internal class YearNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Year?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, recent`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(Year.parse("2023")))
      .shouldBe("{\"value\":\"2023\"}")
  }

  @Test
  fun `serialize, old`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(Year.parse("5")))
      .shouldBe("{\"value\":\"5\"}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, recent`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"2023\" }")
      .shouldBe(MyClass(Year.parse("2023")))
  }

  @Test
  fun `deserialize, old`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"5\" }")
      .shouldBe(MyClass(Year.parse("5")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.readValue<MyClass>("{}")
      .shouldBe(MyClass(null))
  }
}
