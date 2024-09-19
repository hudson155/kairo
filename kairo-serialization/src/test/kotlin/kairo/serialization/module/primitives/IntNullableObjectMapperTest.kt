package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to int serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to ints.
 */
internal class IntNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Int?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, positive`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(42)).shouldBe("{\"value\":42}")
  }

  @Test
  fun `serialize, negative`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(-42)).shouldBe("{\"value\":-42}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, positive`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42))
  }

  @Test
  fun `deserialize, negative`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": -42 }").shouldBe(MyClass(-42))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.readValue<MyClass>("{}").shouldBe(MyClass(null))
  }
}
