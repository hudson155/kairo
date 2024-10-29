package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.Instant
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
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

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, recent`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
      .shouldBe("{\"value\":\"2023-11-13T19:44:32.123456789Z\"}")
  }

  @Test
  fun `serialize, old`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
      .shouldBe("{\"value\":\"0005-01-01T00:00:00Z\"}")
  }

  @Test
  fun `deserialize, recent`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"2023-11-13T19:44:32.123456789Z\" }")
      .shouldBe(MyClass(Instant.parse("2023-11-13T19:44:32.123456789Z")))
  }

  @Test
  fun `deserialize, old`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"0005-01-01T00:00:00Z\" }")
      .shouldBe(MyClass(Instant.parse("0005-01-01T00:00:00.000000000Z")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": null }")
    }
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }

  @Test
  fun `deserialize, missing leading zero from year`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"1-02-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from month`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-2-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from day`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-3T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from hour`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T9:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from minute`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:4:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from second`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:44:2.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low month)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-00-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high month)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-13-03T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low day)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-00T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high day)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-29T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high hour)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T24:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high minute)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:60:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high second)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:44:60.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, too much precision`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:44:32.1234567890Z\" }")
    }
  }

  @Test
  fun `deserialize, no time zone`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:44:32.123456789\" }")
    }
  }

  @Test
  fun `deserialize, non-UTC`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-03T19:44:32.123456789-07:00[America/Edmonton]\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, number`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1699904672123 }")
    }
  }
}
