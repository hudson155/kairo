package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class KotlinDurationSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize((-1234567890987654321L).nanoseconds)
        .shouldBe("\"PT-342935H-31M-30.987654321S\"")
      json.serialize((-8).days)
        .shouldBe("\"PT-192H\"")
      json.serialize((-7).hours)
        .shouldBe("\"PT-7H\"")
      json.serialize((-6).minutes)
        .shouldBe("\"PT-6M\"")
      json.serialize((-5).seconds)
        .shouldBe("\"PT-5S\"")
      json.serialize((-34).milliseconds)
        .shouldBe("\"PT-0.034S\"")
      json.serialize((-12).nanoseconds)
        .shouldBe("\"PT-0.000000012S\"")
      json.serialize(Duration.ZERO)
        .shouldBe("\"PT0S\"")
      json.serialize(12.nanoseconds)
        .shouldBe("\"PT0.000000012S\"")
      json.serialize(34.milliseconds)
        .shouldBe("\"PT0.034S\"")
      json.serialize(5.seconds)
        .shouldBe("\"PT5S\"")
      json.serialize(6.minutes)
        .shouldBe("\"PT6M\"")
      json.serialize(7.hours)
        .shouldBe("\"PT7H\"")
      json.serialize(8.days)
        .shouldBe("\"PT192H\"")
      json.serialize(1234567890987654321L.nanoseconds)
        .shouldBe("\"PT342935H31M30.987654321S\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<Duration>("\"PT-342935H-31M-30.987654321S\"")
        .shouldBe((-1234567890987654321L).nanoseconds)
      json.deserialize<Duration>("\"PT-192H\"")
        .shouldBe((-8).days)
      json.deserialize<Duration>("\"PT-7H\"")
        .shouldBe((-7).hours)
      json.deserialize<Duration>("\"PT-6M\"")
        .shouldBe((-6).minutes)
      json.deserialize<Duration>("\"PT-5S\"")
        .shouldBe((-5).seconds)
      json.deserialize<Duration>("\"PT-0.034S\"")
        .shouldBe((-34).milliseconds)
      json.deserialize<Duration>("\"PT-0.000000012S\"")
        .shouldBe((-12).nanoseconds)
      json.deserialize<Duration>("\"PT0S\"")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("\"PT0.000000012S\"")
        .shouldBe(12.nanoseconds)
      json.deserialize<Duration>("\"PT0.034S\"")
        .shouldBe(34.milliseconds)
      json.deserialize<Duration>("\"PT5S\"")
        .shouldBe(5.seconds)
      json.deserialize<Duration>("\"PT6M\"")
        .shouldBe(6.minutes)
      json.deserialize<Duration>("\"PT7H\"")
        .shouldBe(7.hours)
      json.deserialize<Duration>("\"PT192H\"")
        .shouldBe(8.days)
      json.deserialize<Duration>("\"PT342935H31M30.987654321S\"")
        .shouldBe(1234567890987654321L.nanoseconds)
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<Duration>("-1234567890")
        .shouldBe((-1234567890000000000L).nanoseconds)
      json.deserialize<Duration>("0")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("1234567890")
        .shouldBe(1234567890000000000L.nanoseconds)
    }

  @Test
  fun `deserialize, float`(): Unit =
    runTest {
      json.deserialize<Duration>("-1234567890.987654321")
        .shouldBe((-1234567890987654321).nanoseconds)
      json.deserialize<Duration>("0.0")
        .shouldBe(Duration.ZERO)
      json.deserialize<Duration>("1234567890.987654321")
        .shouldBe(1234567890987654321.nanoseconds)
    }

  @Test
  fun `deserialize, wrong format (missing pt)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Duration>("\"0S\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from String \"0S\"",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Duration>("\"PT 0S\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from String \"PT 0S\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Duration>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.time.Duration(non-null) but was null",
      )

      json.deserialize<Duration?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Duration>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Duration`" +
          " from Array value",
      )
    }
}
