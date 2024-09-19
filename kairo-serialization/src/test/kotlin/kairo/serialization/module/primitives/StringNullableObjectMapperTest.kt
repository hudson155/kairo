package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to string serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to strings.
 */
internal class StringNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: String?,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `serialize, non-empty`(): Unit = runTest {
    mapper.writeValueAsString(MyClass("true")).shouldBe("{\"value\":\"true\"}")
  }

  @Test
  fun `serialize, empty`(): Unit = runTest {
    mapper.writeValueAsString(MyClass("")).shouldBe("{\"value\":\"\"}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.writeValueAsString(MyClass(null)).shouldBe("{\"value\":null}")
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
    mapper.readValue<MyClass>("{ \"value\": null }").shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.readValue<MyClass>("{}").shouldBe(MyClass(null))
  }
}
