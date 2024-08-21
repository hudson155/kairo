package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZoneOffset
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to time zone serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to time zones.
 */
internal class ZoneIdNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: ZoneId?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, UTC offset`() {
    mapper.writeValueAsString(MyClass(ZoneOffset.UTC))
      .shouldBe("{\"value\":\"UTC\"}")
  }

  @Test
  fun `serialize, UTC region`() {
    serializationShouldFail {
      mapper.writeValueAsString(MyClass(ZoneId.of("UTC")))
        .shouldBe("{\"value\":\"UTC\"}")
    }
  }

  @Test
  fun `serialize, non-UTC`() {
    mapper.writeValueAsString(MyClass(ZoneId.of("America/Edmonton")))
      .shouldBe("{\"value\":\"America/Edmonton\"}")
  }

  @Test
  fun `serialize, null`() {
    mapper.writeValueAsString(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, UTC offset`() {
    mapper.readValue<MyClass>("{ \"value\": \"Z\" }")
      .shouldBe(MyClass(ZoneOffset.UTC))
  }

  @Test
  fun `deserialize, UTC region`() {
    mapper.readValue<MyClass>("{ \"value\": \"UTC\" }")
      .shouldBe(MyClass(ZoneOffset.UTC))
  }

  @Test
  fun `deserialize, non-UTC`() {
    mapper.readValue<MyClass>("{ \"value\": \"America/Edmonton\" }")
      .shouldBe(MyClass(ZoneId.of("America/Edmonton")))
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
