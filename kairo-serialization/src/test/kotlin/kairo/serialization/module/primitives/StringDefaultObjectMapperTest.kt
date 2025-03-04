package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to strings.
 */
internal class StringDefaultObjectMapperTest {
  /**
   * This test is specifically for non-nullable properties.
   */
  internal data class MyClass(
    val value: String,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, non-empty`(): Unit = runTest {
    mapper.kairoWrite(MyClass("true")).shouldBe("{\"value\":\"true\"}")
  }

  @Test
  fun `serialize, empty`(): Unit = runTest {
    mapper.kairoWrite(MyClass("")).shouldBe("{\"value\":\"\"}")
  }

  @Test
  fun `deserialize, non-empty`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"true\" }").shouldBe(MyClass("true"))
  }

  @Test
  fun `deserialize, empty`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"\" }").shouldBe(MyClass(""))
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
  fun `deserialize, wrong type, int`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 1 }")
    }
  }

  @Test
  fun `deserialize, wrong type, float`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": 0.0 }")
    }
  }

  @Test
  fun `deserialize, wrong type, boolean`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"value\": true }")
    }
  }
}
