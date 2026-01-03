package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class JavaZonedDateTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(
        LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
          .atZone(ZoneId.of("Etc/GMT+12")),
      ).shouldBe("\"-2023-01-01T00:00:00-12:00[Etc/GMT+12]\"")
      json.serialize(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
          .atZone(ZoneId.of("UTC")),
      ).shouldBe("\"2023-11-14T22:13:20.123456789Z[UTC]\"")
      json.serialize(
        LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
          .atZone(ZoneId.of("Pacific/Kiritimati")),
      ).shouldBe("\"3716-12-30T23:59:59.999999999+14:00[Pacific/Kiritimati]\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<ZonedDateTime>("\"-2023-01-01T00:00:00-12:00[Etc/GMT+12]\"")
        .shouldBe(
          LocalDateTime.of(-2023, Month.JANUARY, 1, 0, 0, 0, 0)
            .atZone(ZoneId.of("Etc/GMT+12")),
        )
      json.deserialize<ZonedDateTime>("\"2023-11-14T22:13:20.123456789Z[UTC]\"")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .atZone(ZoneId.of("UTC")),
        )
      json.deserialize<ZonedDateTime>("\"3716-12-30T23:59:59.999999999+14:00[Pacific/Kiritimati]\"")
        .shouldBe(
          LocalDateTime.of(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999)
            .atZone(ZoneId.of("Pacific/Kiritimati")),
        )
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<ZonedDateTime>("1700000000")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .atZone(ZoneId.of("UTC")),
        )

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("1700000000123456789")
      }
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      json.deserialize<ZonedDateTime>("1700000000.0")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 0)
            .atZone(ZoneId.of("UTC")),
        )

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("1700000000123456789.0")
      }

      json.deserialize<ZonedDateTime>("1700000000.123456789")
        .shouldBe(
          LocalDateTime.of(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789)
            .atZone(ZoneId.of("UTC")),
        )
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-00-14T22:13:20.123456789Z[UTC]\"")
      }

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-13-14T22:13:20.123456789Z[UTC]\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-00T22:13:20.123456789Z[UTC]\"")
      }

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-31T22:13:20.123456789Z[UTC]\"")
      }
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T-01:13:20.123456789Z[UTC]\"")
      }

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T24:13:20.123456789Z[UTC]\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T22:-01:20.123456789Z[UTC]\"")
      }

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T22:60:20.123456789Z[UTC]\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T22:13:-01.123456789Z[UTC]\"")
      }

      shouldThrowAny {
        json.deserialize<ZonedDateTime>("\"2023-11-14T22:13:60.123456789Z[UTC]\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("null")
      }

      json.deserialize<ZonedDateTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZonedDateTime>("""[]""")
      }
    }
}
