package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to local date serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to local dates.
 */
internal class LocalDateNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: LocalDate?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, recent`() {
    mapper.writeValueAsString(MyClass(LocalDate.parse("2023-11-13")))
      .shouldBe("{\"value\":\"2023-11-13\"}")
  }

  @Test
  fun `serialize, old`() {
    mapper.writeValueAsString(MyClass(LocalDate.parse("0005-01-01")))
      .shouldBe("{\"value\":\"0005-01-01\"}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, recent`() {
    mapper.readValue<MyClass>("{ \"value\": \"2023-11-13\" }")
      .shouldBe(MyClass(LocalDate.parse("2023-11-13")))
  }

  @Test
  fun `deserialize, old`() {
    mapper.readValue<MyClass>("{ \"value\": \"0005-01-01\" }")
      .shouldBe(MyClass(LocalDate.parse("0005-01-01")))
  }

  @Test
  fun `deserialize, null`() {
    mapper.readValue<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`() {
    mapper.readValue<MyClass>("{}")
      .shouldBe(MyClass(null))
  }
}
