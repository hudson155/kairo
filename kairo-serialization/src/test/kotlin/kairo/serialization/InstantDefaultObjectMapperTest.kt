package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import java.time.Instant
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to instant serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to instants.
 */
internal class InstantDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: Instant,
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
      mapper.readValue<MyClass>("{ \"value\":\"1-02-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from month`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-2-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from day`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-3T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from hour`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T9:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from minute`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:4:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from second`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:44:2.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low month)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-00-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high month)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-13-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low day)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-00T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high day)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-29T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high hour)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T24:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high minute)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:60:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high second)`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:44:60.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, too much precision`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:44:32.1234567890Z\" }")
    }
  }

  @Test
  fun `deserialize, no time zone`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:44:32.123456789\" }")
    }
  }

  @Test
  fun `deserialize, non-UTC`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\":\"2023-02-03T19:44:32.123456789-07:00[America/Edmonton]\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, number`() {
    deserializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1699904672123 }")
    }
  }
}
