package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BigInteger("-1234567890987654321")).shouldBe("-1234567890987654321")
      json.serialize(BigInteger("0")).shouldBe("0")
      json.serialize(BigInteger("1234567890987654321")).shouldBe("1234567890987654321")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BigInteger>("-1234567890987654321").shouldBe(BigInteger("-1234567890987654321"))
      json.deserialize<BigInteger>("-0").shouldBe(BigInteger("0"))
      json.deserialize<BigInteger>("0").shouldBe(BigInteger("0"))
      json.deserialize<BigInteger>("1234567890987654321").shouldBe(BigInteger("1234567890987654321"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigInteger>("1 234567890987654321")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.math.BigInteger`)"
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigInteger>("01234567890987654321")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<BigInteger>("1e0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (1e0)" +
          " to `java.math.BigInteger` value"
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigInteger>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values"
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigInteger>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'"
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigInteger>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'"
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigInteger>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'"
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigInteger>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.math.BigInteger(non-null) but was null"
      )

      json.deserialize<BigInteger?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigInteger>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Boolean value"
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<BigInteger>("0.0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (0.0)" +
          " to `java.math.BigInteger` value"
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigInteger>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.math.BigInteger` value"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigInteger>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Object value"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigInteger>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Array value"
      )
    }
}
