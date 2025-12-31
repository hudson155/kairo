package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Duration
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DurationSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(Duration.ofNanos(-1234567890987654321))
        .shouldBe("\"PT-342935H-31M-30.987654321S\"")
      jsonMapper.writeValueAsString(Duration.ofDays(-8))
        .shouldBe("\"PT-192H\"")
      jsonMapper.writeValueAsString(Duration.ofHours(-7))
        .shouldBe("\"PT-7H\"")
      jsonMapper.writeValueAsString(Duration.ofMinutes(-6))
        .shouldBe("\"PT-6M\"")
      jsonMapper.writeValueAsString(Duration.ofSeconds(-5))
        .shouldBe("\"PT-5S\"")
      jsonMapper.writeValueAsString(Duration.ofMillis(-34))
        .shouldBe("\"PT-0.034S\"")
      jsonMapper.writeValueAsString(Duration.ofNanos(-12))
        .shouldBe("\"PT-0.000000012S\"")
      jsonMapper.writeValueAsString(Duration.ZERO)
        .shouldBe("\"PT0S\"")
      jsonMapper.writeValueAsString(Duration.ofNanos(12))
        .shouldBe("\"PT0.000000012S\"")
      jsonMapper.writeValueAsString(Duration.ofMillis(34))
        .shouldBe("\"PT0.034S\"")
      jsonMapper.writeValueAsString(Duration.ofSeconds(5))
        .shouldBe("\"PT5S\"")
      jsonMapper.writeValueAsString(Duration.ofMinutes(6))
        .shouldBe("\"PT6M\"")
      jsonMapper.writeValueAsString(Duration.ofHours(7))
        .shouldBe("\"PT7H\"")
      jsonMapper.writeValueAsString(Duration.ofDays(8))
        .shouldBe("\"PT192H\"")
      jsonMapper.writeValueAsString(Duration.ofNanos(1234567890987654321))
        .shouldBe("\"PT342935H31M30.987654321S\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      jsonMapper.readValue<Duration>("\"PT-342935H-31M-30.987654321S\"")
        .shouldBe(Duration.ofNanos(-1234567890987654321))
      jsonMapper.readValue<Duration>("\"PT-192H\"")
        .shouldBe(Duration.ofDays(-8))
      jsonMapper.readValue<Duration>("\"PT-7H\"")
        .shouldBe(Duration.ofHours(-7))
      jsonMapper.readValue<Duration>("\"PT-6M\"")
        .shouldBe(Duration.ofMinutes(-6))
      jsonMapper.readValue<Duration>("\"PT-5S\"")
        .shouldBe(Duration.ofSeconds(-5))
      jsonMapper.readValue<Duration>("\"PT-0.034S\"")
        .shouldBe(Duration.ofMillis(-34))
      jsonMapper.readValue<Duration>("\"PT-0.000000012S\"")
        .shouldBe(Duration.ofNanos(-12))
      jsonMapper.readValue<Duration>("\"PT0S\"")
        .shouldBe(Duration.ZERO)
      jsonMapper.readValue<Duration>("\"PT0.000000012S\"")
        .shouldBe(Duration.ofNanos(12))
      jsonMapper.readValue<Duration>("\"PT0.034S\"")
        .shouldBe(Duration.ofMillis(34))
      jsonMapper.readValue<Duration>("\"PT5S\"")
        .shouldBe(Duration.ofSeconds(5))
      jsonMapper.readValue<Duration>("\"PT6M\"")
        .shouldBe(Duration.ofMinutes(6))
      jsonMapper.readValue<Duration>("\"PT7H\"")
        .shouldBe(Duration.ofHours(7))
      jsonMapper.readValue<Duration>("\"PT192H\"")
        .shouldBe(Duration.ofDays(8))
      jsonMapper.readValue<Duration>("\"PT342935H31M30.987654321S\"")
        .shouldBe(Duration.ofNanos(1234567890987654321))
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      jsonMapper.readValue<Duration>("-1234567890")
        .shouldBe(Duration.ofNanos(-1234567890000000000))
      jsonMapper.readValue<Duration>("0")
        .shouldBe(Duration.ZERO)
      jsonMapper.readValue<Duration>("1234567890")
        .shouldBe(Duration.ofNanos(1234567890000000000))
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      jsonMapper.readValue<Duration>("-1234567890.0")
        .shouldBe(Duration.ofNanos(-1234567890000000000))
      jsonMapper.readValue<Duration>("0.0")
        .shouldBe(Duration.ZERO)
      jsonMapper.readValue<Duration>("1234567890.0")
        .shouldBe(Duration.ofNanos(1234567890000000000))
    }

  @Test
  fun `deserialize, wrong format (missing pt)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("\"0S\"")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("\"PT 0S\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("null")
      }

      jsonMapper.readValue<Duration?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Duration>("""[]""")
      }
    }
}
