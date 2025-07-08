package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import java.time.YearMonth
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to year-month serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to year-months.
 */
internal class YearMonthNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: YearMonth?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, recent`(): Unit = runTest {
    mapper.kairoWrite(MyClass(YearMonth.parse("2023-11")))
      .shouldBe("{\"value\":\"2023-11\"}")
  }

  @Test
  fun `serialize, old`(): Unit = runTest {
    mapper.kairoWrite(MyClass(YearMonth.parse("0005-01")))
      .shouldBe("{\"value\":\"0005-01\"}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.kairoWrite(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, recent`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": \"2023-11\" }")
      .shouldBe(MyClass(YearMonth.parse("2023-11")))
  }

  @Test
  fun `deserialize, old`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": \"0005-01\" }")
      .shouldBe(MyClass(YearMonth.parse("0005-01")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{}")
      .shouldBe(MyClass(null))
  }
}
