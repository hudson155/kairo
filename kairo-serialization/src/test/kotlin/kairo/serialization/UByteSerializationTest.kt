package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.exc.InputCoercionException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UByteSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(0U.toUByte()).shouldBe("0")
      json.serialize(170U.toUByte()).shouldBe("170")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<UByte>("-0").shouldBe(0U.toUByte())
      json.deserialize<UByte>("0").shouldBe(0U.toUByte())
      json.deserialize<UByte>("170").shouldBe(170U.toUByte())
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<UByte>("-1")
      }.message.shouldStartWith(
        "Numeric value (-1) out of range of UByte (0 - 255).",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<UByte>("256")
      }.message.shouldStartWith(
        "Numeric value (256) out of range of UByte (0 - 255).",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      // Passes due to DeserializationFeature.FAIL_ON_TRAILING_TOKENS being disabled
      json.deserialize<UByte>("1 70").shouldBe(1.toUByte())
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("0170")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<UByte>("1e0").shouldBe(1U.toUByte())
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<UByte>("0.0").shouldBe(0U.toUByte())
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UByte>("\"0\"")
      }.message.shouldStartWith(
        "Current token (VALUE_STRING) not numeric, can not use numeric value accessors",
      )
    }
}
