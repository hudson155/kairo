package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.Duration
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class JavaDurationSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Duration.ofNanos(-1234567890987654321))
        .shouldBe("\"PT-342935H-31M-30.987654321S\"")
      json.serialize(Duration.ofDays(-8))
        .shouldBe("\"PT-192H\"")
      json.serialize(Duration.ofHours(-7))
        .shouldBe("\"PT-7H\"")
      json.serialize(Duration.ofMinutes(-6))
        .shouldBe("\"PT-6M\"")
      json.serialize(Duration.ofSeconds(-5))
        .shouldBe("\"PT-5S\"")
      json.serialize(Duration.ofMillis(-34))
        .shouldBe("\"PT-0.034S\"")
      json.serialize(Duration.ofNanos(-12))
        .shouldBe("\"PT-0.000000012S\"")
      json.serialize(Duration.ZERO)
        .shouldBe("\"PT0S\"")
      json.serialize(Duration.ofNanos(12))
        .shouldBe("\"PT0.000000012S\"")
      json.serialize(Duration.ofMillis(34))
        .shouldBe("\"PT0.034S\"")
      json.serialize(Duration.ofSeconds(5))
        .shouldBe("\"PT5S\"")
      json.serialize(Duration.ofMinutes(6))
        .shouldBe("\"PT6M\"")
      json.serialize(Duration.ofHours(7))
        .shouldBe("\"PT7H\"")
      json.serialize(Duration.ofDays(8))
        .shouldBe("\"PT192H\"")
      json.serialize(Duration.ofNanos(1234567890987654321))
        .shouldBe("\"PT342935H31M30.987654321S\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<Duration>("\"PT-342935H-31M-30.987654321S\"")
        .shouldBe(Duration.ofNanos(-1234567890987654321))
      json.deserialize<Duration>("\"PT-192H\"")
        .shouldBe(Duration.ofDays(-8))
      json.deserialize<Duration>("\"PT-7H\"")
        .shouldBe(Duration.ofHours(-7))
      json.deserialize<Duration>("\"PT-6M\"")
        .shouldBe(Duration.ofMinutes(-6))
      json.deserialize<Duration>("\"PT-5S\"")
        .shouldBe(Duration.ofSeconds(-5))
      json.deserialize<Duration>("\"PT-0.034S\"")
        .shouldBe(Duration.ofMillis(-34))
      json.deserialize<Duration>("\"PT-0.000000012S\"")
        .shouldBe(Duration.ofNanos(-12))
      json.deserialize<Duration>("\"PT0S\"")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("\"PT0.000000012S\"")
        .shouldBe(Duration.ofNanos(12))
      json.deserialize<Duration>("\"PT0.034S\"")
        .shouldBe(Duration.ofMillis(34))
      json.deserialize<Duration>("\"PT5S\"")
        .shouldBe(Duration.ofSeconds(5))
      json.deserialize<Duration>("\"PT6M\"")
        .shouldBe(Duration.ofMinutes(6))
      json.deserialize<Duration>("\"PT7H\"")
        .shouldBe(Duration.ofHours(7))
      json.deserialize<Duration>("\"PT192H\"")
        .shouldBe(Duration.ofDays(8))
      json.deserialize<Duration>("\"PT342935H31M30.987654321S\"")
        .shouldBe(Duration.ofNanos(1234567890987654321))
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<Duration>("-1234567890")
        .shouldBe(Duration.ofNanos(-1234567890000000000))
      json.deserialize<Duration>("0")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("1234567890")
        .shouldBe(Duration.ofNanos(1234567890000000000))
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      json.deserialize<Duration>("-1234567890.987654321")
        .shouldBe(Duration.ofNanos(-1234567890987654321))
      json.deserialize<Duration>("0.0")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("1234567890.987654321")
        .shouldBe(Duration.ofNanos(1234567890987654321))
    }

  @Test
  fun `deserialize, wrong format (missing pt)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Duration>("\"0S\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from String \"0S\""
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Duration>("\"PT 0S\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from String \"PT 0S\""
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Duration>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.Duration(non-null) but was null"
      )

      json.deserialize<Duration?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from Array value"
      )
    }
}
