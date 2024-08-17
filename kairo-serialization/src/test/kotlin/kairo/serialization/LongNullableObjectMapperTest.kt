package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to long serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to longs.
 */
internal class LongNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Long?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, positive`() {
    mapper.writeValueAsString(MyClass(42L)).shouldBe("{\"value\":42}")
  }

  @Test
  fun `serialize, negative`() {
    mapper.writeValueAsString(MyClass(-42L)).shouldBe("{\"value\":-42}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, positive`() {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(42L))
  }

  @Test
  fun `deserialize, negative`() {
    mapper.readValue<MyClass>("{ \"value\": -42 }").shouldBe(MyClass(-42L))
  }

  @Test
  fun `deserialize, null`() {
    mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`() {
    mapper.readValue<MyClass>("{}").shouldBe(MyClass(null))
  }
}
