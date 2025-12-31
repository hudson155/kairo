package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class OffsetTimeSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(LocalTime.of(0, 0, 0, 0).atOffset(ZoneOffset.MIN))
        .shouldBe("\"00:00-18:00\"")
      jsonMapper.writeValueAsString(LocalTime.of(22, 13, 20, 123456789).atOffset(ZoneOffset.UTC))
        .shouldBe("\"22:13:20.123456789Z\"")
      jsonMapper.writeValueAsString(LocalTime.of(23, 59, 59, 999999999).atOffset(ZoneOffset.MAX))
        .shouldBe("\"23:59:59.999999999+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<OffsetTime>("\"00:00-18:00\"")
        .shouldBe(LocalTime.of(0, 0, 0, 0).atOffset(ZoneOffset.MIN))
      jsonMapper.readValue<OffsetTime>("\"22:13:20.123456789Z\"")
        .shouldBe(LocalTime.of(22, 13, 20, 123456789).atOffset(ZoneOffset.UTC))
      jsonMapper.readValue<OffsetTime>("\"23:59:59.999999999+18:00\"")
        .shouldBe(LocalTime.of(23, 59, 59, 999999999).atOffset(ZoneOffset.MAX))
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"-01:13:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"24:13:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"22:-01:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"22:60:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"22:13:-01.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("\"22:13:60.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("null")
      }

      jsonMapper.readValue<OffsetTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("221360123456789")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("221360123456789.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<OffsetTime>("""[]""")
      }
    }
}
