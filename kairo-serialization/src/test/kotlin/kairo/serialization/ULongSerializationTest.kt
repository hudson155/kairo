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

internal class ULongSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(0UL).shouldBe("0")
      json.serialize(9223372046731319018UL).shouldBe("9223372046731319018")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ULong>("-0").shouldBe(0UL)
      json.deserialize<ULong>("0").shouldBe(0UL)
      json.deserialize<ULong>("9223372046731319018").shouldBe(9223372046731319018UL)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<ULong>("-1")
      }.message.shouldStartWith(
        "Numeric value (-1) out of range of ULong (0 - 18446744073709551615).",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<ULong>("18446744073709551616")
      }.message.shouldStartWith(
        "Numeric value (18446744073709551616) out of range of ULong (0 - 18446744073709551615).",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ULong>("9 223372046731319018")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `kotlin.ULong`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("09223372046731319018")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<ULong>("1e0").shouldBe(1UL)
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<ULong>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.ULong(non-null) but was null",
      )

      json.deserialize<ULong?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("true")
      }.message.shouldStartWith(
        "Current token (VALUE_TRUE) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<ULong>("0.0").shouldBe(0UL)
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("\"0\"")
      }.message.shouldStartWith(
        "Current token (VALUE_STRING) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("""{}""")
      }.message.shouldStartWith(
        "Current token (START_OBJECT) not numeric, can not use numeric value accessors",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("""[]""")
      }.message.shouldStartWith(
        "Current token (START_ARRAY) not numeric, can not use numeric value accessors",
      )
    }
}
