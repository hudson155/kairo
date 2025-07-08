package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoRead
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to float serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to floats.
 */
internal class FloatNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Float?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, positive`(): Unit = runTest {
    mapper.kairoWrite(MyClass(1.23F)).shouldBe("{\"value\":1.23}")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.kairoWrite(MyClass(-1.23F)).shouldBe("{\"value\":-1.23}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.kairoWrite(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": 1.23 }").shouldBe(MyClass(1.23F))
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": -1.23 }").shouldBe(MyClass(-1.23F))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{ \"value\": null }").shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.kairoRead<MyClass>("{}").shouldBe(MyClass(null))
  }
}
