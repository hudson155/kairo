package kairo.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.util.Optional
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to optional serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to optionals.
 *
 * The behaviour here might seem a bit odd at first,
 * since null is serialized to missing and Optional.empty() is serialized to null.
 * However, this approach is in fact necessary to provide consistency between nullable and non-nullable Optionals.
 */
internal class OptionalDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  internal data class MyClass(
    val value: Optional<Int>,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, present`() {
    mapper.writeValueAsString(MyClass(Optional.of(42))).shouldBe("{\"value\":42}")
  }

  @Test
  fun `serialize, empty`() {
    mapper.writeValueAsString(MyClass(Optional.empty())).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, present`() {
    mapper.readValue<MyClass>("{ \"value\": 42 }").shouldBe(MyClass(Optional.of(42)))
  }

  @Test
  fun `deserialize, null`() {
    mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(Optional.empty()))
  }

  @Test
  fun `deserialize, missing`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, wrong type, float`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1.23 }")
    }
  }

  @Test
  fun `deserialize, wrong type, string`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"42\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
