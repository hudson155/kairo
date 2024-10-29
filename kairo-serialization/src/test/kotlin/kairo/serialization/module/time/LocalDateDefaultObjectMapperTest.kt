package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
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

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, recent`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(LocalDate.parse("2023-11-13")))
      .shouldBe("{\"value\":\"2023-11-13\"}")
  }

  @Test
  fun `serialize, old`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(LocalDate.parse("0005-01-01")))
      .shouldBe("{\"value\":\"0005-01-01\"}")
  }

  @Test
  fun `deserialize, recent`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"2023-11-13\" }")
      .shouldBe(MyClass(LocalDate.parse("2023-11-13")))
  }

  @Test
  fun `deserialize, old`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"0005-01-01\" }")
      .shouldBe(MyClass(LocalDate.parse("0005-01-01")))
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
  fun `deserialize, has time`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-11-13T19:44:32.123456789Z\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from year`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"1-02-03\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from month`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-2-03\" }")
    }
  }

  @Test
  fun `deserialize, missing leading zero from day`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-3\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low month)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-00-03\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high month)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-13-03\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (low day)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-00\" }")
    }
  }

  @Test
  fun `deserialize, nonexistent date (high day)`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"2023-02-29\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, number`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 20231113 }")
    }
  }
}
