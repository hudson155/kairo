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

internal class ShortSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize((-155).toShort()).shouldBe("-155")
      json.serialize(0.toShort()).shouldBe("0")
      json.serialize(155.toShort()).shouldBe("155")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Short>("-155").shouldBe((-155).toShort())
      json.deserialize<Short>("-0").shouldBe(0.toShort())
      json.deserialize<Short>("0").shouldBe(0.toShort())
      json.deserialize<Short>("155").shouldBe(155.toShort())
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Short>("-32769")
      }.message.shouldStartWith(
        "Numeric value (-32769) out of range of Java short",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Short>("32768")
      }.message.shouldStartWith(
        "Numeric value (32768) out of range of Java short",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Short>("1 55")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.lang.Short`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Short>("0155")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Short>("1e0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (1e0)" +
          " to `java.lang.Short` value",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Short>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Short>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Short>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Short>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Short>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Short(non-null)" +
          " but was null",
      )

      json.deserialize<Short?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Short>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Short`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Short>("0.0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (0.0)" +
          " to `java.lang.Short` value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Short>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.lang.Short` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Short>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Short`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Short>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Short`" +
          " from Array value",
      )
    }
}
