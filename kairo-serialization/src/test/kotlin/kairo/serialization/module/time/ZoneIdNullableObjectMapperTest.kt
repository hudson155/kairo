package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZoneOffset
import kairo.serialization.jsonMapper
import kairo.serialization.serializationShouldFail
import kairo.serialization.util.kairoWrite
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to time zone serialization/deserialization.
 * Therefore, some test cases (such as unknown properties, pretty printing) are not included
 * since they are not strictly related to time zones.
 */
internal class ZoneIdNullableObjectMapperTest {
  /**
   * This test is specifically for nullable properties.
   */
  internal data class MyClass(
    val value: ZoneId?,
  )

  private val mapper: JsonMapper = jsonMapper().build()

  @Test
  fun `serialize, UTC offset`(): Unit = runTest {
    mapper.kairoWrite(MyClass(ZoneOffset.UTC))
      .shouldBe("{\"value\":\"UTC\"}")
  }

  @Test
  fun `serialize, UTC region`(): Unit = runTest {
    serializationShouldFail {
      mapper.kairoWrite(MyClass(ZoneId.of("UTC")))
    }
  }

  @Test
  fun `serialize, non-UTC`(): Unit = runTest {
    mapper.kairoWrite(MyClass(ZoneId.of("America/Edmonton")))
      .shouldBe("{\"value\":\"America/Edmonton\"}")
  }

  @Test
  fun `serialize, null`(): Unit = runTest {
    mapper.kairoWrite(MyClass(null))
      .shouldBe("{\"value\":null}")
  }

  @Test
  fun `deserialize, UTC offset`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"Z\" }")
      .shouldBe(MyClass(ZoneOffset.UTC))
  }

  @Test
  fun `deserialize, UTC region`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"UTC\" }")
      .shouldBe(MyClass(ZoneOffset.UTC))
  }

  @Test
  fun `deserialize, non-UTC`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": \"America/Edmonton\" }")
      .shouldBe(MyClass(ZoneId.of("America/Edmonton")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"value\": null }")
      .shouldBe(MyClass(null))
  }

  @Test
  fun `deserialize, missing`(): Unit = runTest {
    mapper.readValue<MyClass>("{}")
      .shouldBe(MyClass(null))
  }
}
