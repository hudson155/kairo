package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to local date serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to local dates.
 */
internal class LocalDateDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: LocalDate,
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
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, missing leading zero from year`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"1-02-03\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from month`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-2-03\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from day`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-3\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low month)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-00-03\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high month)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-13-03\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low day)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-00\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high day)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-29\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, number`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 20231113 }")
    }
  }
}
