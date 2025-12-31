package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InstantSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(
        LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
          .toInstant(ZoneOffset.UTC),
      ).shouldBe("\"-2023-01-01T00:00:00Z\"")
      jsonMapper.writeValueAsString(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .toInstant(ZoneOffset.UTC),
      ).shouldBe("\"2023-11-14T22:13:20.123456789Z\"")
      jsonMapper.writeValueAsString(
        LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
          .toInstant(ZoneOffset.UTC),
      ).shouldBe("\"3716-12-30T23:59:59.999999999Z\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      jsonMapper.readValue<Instant>(
        "\"-2023-01-01T00:00:00Z\""
      ).shouldBe(
        LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
          .toInstant(ZoneOffset.UTC),
      )
      jsonMapper.readValue<Instant>(
        "\"2023-11-14T22:13:20.123456789Z\""
      ).shouldBe(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .toInstant(ZoneOffset.UTC),
      )
      jsonMapper.readValue<Instant>(
        "\"3716-12-30T23:59:59.999999999Z\""
      ).shouldBe(
        LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
          .toInstant(ZoneOffset.UTC),
      )
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      jsonMapper.readValue<Instant>(
        "1700000000"
      ).shouldBe(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
          .toInstant(ZoneOffset.UTC),
      )

      shouldThrowAny {
        jsonMapper.readValue<Instant>("1700000000123456789")
      }
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      jsonMapper.readValue<Instant>(
        "1700000000.0"
      ).shouldBe(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
          .toInstant(ZoneOffset.UTC),
      )

      shouldThrowAny {
        jsonMapper.readValue<Instant>("1700000000123456789.0")
      }

      jsonMapper.readValue<Instant>(
        "1700000000.123456789"
      ).shouldBe(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .toInstant(ZoneOffset.UTC),
      )
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-00-14T22:13:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-13-14T22:13:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-00T22:13:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-31T22:13:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T-01:13:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T24:13:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T22:-01:20.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T22:60:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T22:13:-01.123456789Z\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Instant>("\"2023-11-14T22:13:60.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("null")
      }

      jsonMapper.readValue<Instant?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Instant>("""[]""")
      }
    }
}
