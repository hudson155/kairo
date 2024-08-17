package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to boolean serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to booleans.
 */
internal class BooleanNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Boolean?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, false`() {
    mapper.writeValueAsString(MyClass(false)).shouldBe("{\"value\":false}")
  }

  @Test
  fun `serialize, true`() {
    mapper.writeValueAsString(MyClass(true)).shouldBe("{\"value\":true}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, false`() {
    mapper.readValue<MyClass>("{ \"value\": false }").shouldBe(MyClass(false))
  }

  @Test
  fun `deserialize, true`() {
    mapper.readValue<MyClass>("{ \"value\": true }").shouldBe(MyClass(true))
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
