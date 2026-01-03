package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalTime
import java.time.OffsetTime
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class JavaOffsetTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalTime.of(0, 0, 0, 0).atOffset(ZoneOffset.MIN))
        .shouldBe("\"00:00-18:00\"")
      json.serialize(LocalTime.of(22, 13, 20, 123456789).atOffset(ZoneOffset.UTC))
        .shouldBe("\"22:13:20.123456789Z\"")
      json.serialize(LocalTime.of(23, 59, 59, 999999999).atOffset(ZoneOffset.MAX))
        .shouldBe("\"23:59:59.999999999+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<OffsetTime>("\"00:00-18:00\"")
        .shouldBe(LocalTime.of(0, 0, 0, 0).atOffset(ZoneOffset.MIN))
      json.deserialize<OffsetTime>("\"22:13:20.123456789Z\"")
        .shouldBe(LocalTime.of(22, 13, 20, 123456789).atOffset(ZoneOffset.UTC))
      json.deserialize<OffsetTime>("\"23:59:59.999999999+18:00\"")
        .shouldBe(LocalTime.of(23, 59, 59, 999999999).atOffset(ZoneOffset.MAX))
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("\"-01:13:20.123456789Z\"")
      }

      shouldThrowAny {
        json.deserialize<OffsetTime>("\"24:13:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("\"22:-01:20.123456789Z\"")
      }

      shouldThrowAny {
        json.deserialize<OffsetTime>("\"22:60:20.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("\"22:13:-01.123456789Z\"")
      }

      shouldThrowAny {
        json.deserialize<OffsetTime>("\"22:13:60.123456789Z\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("null")
      }

      json.deserialize<OffsetTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("221360123456789")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<OffsetTime>("""[]""")
      }
    }
}
