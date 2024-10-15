package kairo.serialization.module.time

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZoneOffset
import kairo.serialization.ObjectMapperFactory
import kairo.serialization.ObjectMapperFormat
import kairo.serialization.serializationShouldFail
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is intended to test behaviour strictly related to time zone map key serialization/deserialization.
 * Therefore, some test cases are not included
 * since they are not strictly related to time zone map keys.
 */
internal class ZoneIdKeyObjectMapperTest {
  internal data class MyClass(
    val values: Map<ZoneId?, String>,
  )

  private val mapper: JsonMapper = ObjectMapperFactory.builder(ObjectMapperFormat.Json).build()

  @Test
  fun `deserialize, UTC offset`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"Z\": \"value\" } }")
      .shouldBe(MyClass(mapOf(ZoneOffset.UTC to "value")))
  }

  @Test
  fun `deserialize, UTC region`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"UTC\": \"value\" } }")
      .shouldBe(MyClass(mapOf(ZoneOffset.UTC to "value")))
  }

  @Test
  fun `deserialize, non-UTC`(): Unit = runTest {
    mapper.readValue<MyClass>("{ \"values\": { \"America/Edmonton\": \"value\" } }")
      .shouldBe(MyClass(mapOf(ZoneId.of("America/Edmonton") to "value")))
  }

  @Test
  fun `deserialize, null`(): Unit = runTest {
    serializationShouldFail {
      mapper.readValue<MyClass>("{ \"values\": { null: \"value\" } }")
    }
  }
}
