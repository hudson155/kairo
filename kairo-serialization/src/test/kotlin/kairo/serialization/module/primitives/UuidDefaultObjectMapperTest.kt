package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
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
    val value: Uuid,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, default`(): Unit = runTest {
    mapper.kairoWrite(MyClass(Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
      .shouldBe("{\"value\":\"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\"}")
  }

  @Test
  fun `deserialize, default`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8\" }")
      .shouldBe(MyClass(Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
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
  fun `deserialize, too short`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f\" }")
    }
  }

  @Test
  fun `deserialize, too long`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45f88\" }")
    }
  }

  @Test
  fun `deserialize, missing dashes`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"3ec0a853dae34ee1abe20b9c7dee45f8\" }")
      .shouldBe(MyClass(Uuid.parse("3ec0a853-dae3-4ee1-abe2-0b9c7dee45f8")))
  }

  @Test
  fun `deserialize, invalid character`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": \"3ec0a853-dae3-4ee1-abe2-0b9c7dee45fg\" }")
    }
  }

  @Test
  fun `deserialize, wrong type, int`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 42 }")
    }
  }

  @Test
  fun `deserialize, wrong type, float`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1.23 }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
