package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to UUID serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to UUIDs.
 */
internal class UuidDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: UUID,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, default`() {
    mapper.writeValueAsString(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
      .shouldBe("{\"value\":\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\"}")
  }

  @Test
  fun `deserialize, default`() {
    mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\" }")
      .shouldBe(MyClass(UUID.fromString("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
  }

  @Test
  fun `deserialize, null`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, too short`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f\" }")
    }
  }

  @Test
  fun `deserialize, too long`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f88\" }")
    }
  }

  @Test
  fun `deserialize, missing dashes`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853dae34ee1abe20b9c7dee45f8\" }")
    }
  }

  @Test
  fun `deserialize, invalid character`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45fg\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, int`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": 42 }")
    }
  }

  @Test
  fun `deserialize, wrong type, float`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": 1.23 }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`() {
    shouldThrow<JsonMappingException> {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
