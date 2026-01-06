package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.exc.InputCoercionException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UIntSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(0U).shouldBe("0")
      json.serialize(2147573858U).shouldBe("2147573858")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<UInt>("-0").shouldBe(0U)
      json.deserialize<UInt>("0").shouldBe(0U)
      json.deserialize<UInt>("2147573858").shouldBe(2147573858U)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<UInt>("-1")
      }.message.shouldStartWith(
        "Numeric value (-1) out of range of UInt (0 - 4294967295).",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<UInt>("4294967296")
      }.message.shouldStartWith(
        "Numeric value (4294967296) out of range of UInt (0 - 4294967295).",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<UInt>("2 147573858")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `kotlin.UInt`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("02147573858")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<UInt>("1e0").shouldBe(1U)
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<UInt>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.UInt(non-null) but was null",
      )

      json.deserialize<UInt?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("true")
      }.message.shouldStartWith(
        "Current token (VALUE_TRUE) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<UInt>("0.0").shouldBe(0U)
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("\"0\"")
      }.message.shouldStartWith(
        "Current token (VALUE_STRING) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("""{}""")
      }.message.shouldStartWith(
        "Current token (START_OBJECT) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<UInt>("""[]""")
      }.message.shouldStartWith(
        "Current token (START_ARRAY) not numeric, can not use numeric value accessors",
      )
    }
}
