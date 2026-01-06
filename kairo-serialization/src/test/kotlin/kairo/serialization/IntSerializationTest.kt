package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.exc.InputCoercionException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class IntSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(-90210).shouldBe("-90210")
      json.serialize(0).shouldBe("0")
      json.serialize(90210).shouldBe("90210")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Int>("-90210").shouldBe(-90210)
      json.deserialize<Int>("-0").shouldBe(0)
      json.deserialize<Int>("0").shouldBe(0)
      json.deserialize<Int>("90210").shouldBe(90210)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Int>("-2147483649")
      }.message.shouldStartWith(
        "Numeric value (-2147483649) out of range of int (-2147483648 - 2147483647)",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Int>("2147483648")
      }.message.shouldStartWith(
        "Numeric value (2147483648) out of range of int (-2147483648 - 2147483647)",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("9 0210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("090210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Int>("1e0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (1e0)" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Int>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Int>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Int(non-null)" +
          " but was null",
      )

      json.deserialize<Int?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Int>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Integer`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Int>("0.0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (0.0)" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Int>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.lang.Integer` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Int>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Integer`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Int>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Integer`" +
          " from Array value",
      )
    }
}
