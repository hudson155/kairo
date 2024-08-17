package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.BooleanNullableObjectMapperTest.MyClass
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to strings.
 */
internal class StringNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: String?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, non-empty`() {
    mapper.writeValueAsString(MyClass("true")).shouldBe("{\"value\":\"true\"}")
  }

  @Test
  fun `serialize, negative`() {
    mapper.writeValueAsString(MyClass("")).shouldBe("{\"value\":\"\"}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, non-empty`() {
    mapper.readValue<MyClass>("{ \"value\": \"true\" }").shouldBe(MyClass("true"))
  }

  @Test
  fun `deserialize, empty`() {
    mapper.readValue<MyClass>("{ \"value\": \"\" }").shouldBe(MyClass(""))
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
