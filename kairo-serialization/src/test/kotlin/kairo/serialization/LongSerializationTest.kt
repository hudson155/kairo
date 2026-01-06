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
internal class LongSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(-4032816692L).shouldBe("-4032816692")
      json.serialize(0L).shouldBe("0")
      json.serialize(4032816692L).shouldBe("4032816692")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Long>("-4032816692").shouldBe(-4032816692L)
      json.deserialize<Long>("-0").shouldBe(0L)
      json.deserialize<Long>("0").shouldBe(0L)
      json.deserialize<Long>("4032816692").shouldBe(4032816692L)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Long>("-9223372036854775809")
      }.message.shouldStartWith(
        "Numeric value (-9223372036854775809) out of range of long (-9223372036854775808 - 9223372036854775807)",
      )

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Long>("9223372036854775808")
      }.message.shouldStartWith(
        "Numeric value (9223372036854775808) out of range of long (-9223372036854775808 - 9223372036854775807)",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Long>("9 876543210")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.lang.Long`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Long>("04032816692")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Long>("1e0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (1e0)" +
          " to `java.lang.Long` value",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Long>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Long>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Long>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Long>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Long>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Long(non-null)" +
          " but was null",
      )

      json.deserialize<Long?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Long>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Long`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Long>("0.0")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (0.0)" +
          " to `java.lang.Long` value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Long>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.lang.Long` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Long>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Long`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Long>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Long`" +
          " from Array value",
      )
    }
}
