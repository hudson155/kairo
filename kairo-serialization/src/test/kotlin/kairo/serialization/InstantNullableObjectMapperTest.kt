package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to instant serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to instants.
 */
internal class InstantNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Instant?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, recent`() {
    mapper.writeValueAsString(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
      .shouldBe("{\"value\":\"2023-11-13T19:44:32.123456789Z\"}")
  }

  @Test
  fun `serialize, old`() {
    mapper.writeValueAsString(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
      .shouldBe("{\"value\":\"0005-01-01T00:00:00Z\"}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, recent`() {
    mapper.readValue<MyClass>("{ \"value\": \"2023-11-13T19:44:32.123456789Z\" }")
      .shouldBe(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
  }

  @Test
  fun `deserialize, old`() {
    mapper.readValue<MyClass>("{ \"value\": \"0005-01-01T00:00:00Z\" }")
      .shouldBe(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
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
