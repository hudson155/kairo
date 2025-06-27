package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to boolean serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to booleans.
 *
 * Jackson has some default behaviours with is-prefixed properties, where it serializes [MyClass.isValue] to "value"
 * (dropping the "is" prefix and handling case corrections).
 * The main purpose of this test is to ensure that behaviour is disabled.
 */
internal class BooleanIsObjectMapperTest {
  /**
   * This test is specifically for is-prefixed properties.
   */
  internal data class MyClass(
    val isValue: Boolean,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, false`(): Unit = runTest {
    mapper.kairoWrite(MyClass(false)).shouldBe("{\"isValue\":false}")
  }

  @Test
  fun `serialize, true`(): Unit = runTest {
    mapper.kairoWrite(MyClass(true)).shouldBe("{\"isValue\":true}")
  }

  @Test
  fun `deserialize, false`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"isValue\": false }").shouldBe(MyClass(false))
  }

  @Test
  fun `deserialize, true`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"isValue\": true }").shouldBe(MyClass(true))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"isValue\": null }")
    }
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{}")
    }
  }
}
