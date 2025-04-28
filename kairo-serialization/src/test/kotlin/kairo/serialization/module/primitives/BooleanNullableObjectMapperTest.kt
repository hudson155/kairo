package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.jsonMapper
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to boolean serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to booleans.
 */
internal class BooleanNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: Boolean?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, false`(): Unit = runTest {
    mapper.kairoWrite(MyClass(false)).shouldBe("{\"value\":false}")
  }

  @Test
  fun `serialize, true`(): Unit = runTest {
    mapper.kairoWrite(MyClass(true)).shouldBe("{\"value\":true}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.kairoWrite(MyClass(null)).shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, false`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": false }").shouldBe(MyClass(false))
  }

  @Test
  fun `deserialize, true`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": true }").shouldBe(MyClass(true))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.readValue<MyClass>("{}").shouldBe(MyClass(null))
  }
}
