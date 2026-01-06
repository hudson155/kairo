package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class FloatSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(-90210F).shouldBe("-90210.0")
      json.serialize(-3.14F).shouldBe("-3.14")
      json.serialize(0F).shouldBe("0.0")
      json.serialize(3.14F).shouldBe("3.14")
      json.serialize(90210F).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Float>("-90210.0").shouldBe(-90210F)
      json.deserialize<Float>("-90210").shouldBe(-90210F)
      json.deserialize<Float>("-3.14").shouldBe(-3.14F)
      json.deserialize<Float>("-1e0").shouldBe(-1F)
      json.deserialize<Float>("-0.0").shouldBe(0F)
      json.deserialize<Float>("-0").shouldBe(0F)
      json.deserialize<Float>("0").shouldBe(0F)
      json.deserialize<Float>("0.0").shouldBe(0F)
      json.deserialize<Float>("1e0").shouldBe(1F)
      json.deserialize<Float>("3.14").shouldBe(3.14F)
      json.deserialize<Float>("90210").shouldBe(90210F)
      json.deserialize<Float>("90210.0").shouldBe(90210F)
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("9 0210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("3.1 4")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.lang.Float`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("090210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("03.14")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Float>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Float(non-null)" +
          " but was null",
      )

      json.deserialize<Float?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Float`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.lang.Float` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Float`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Float`" +
          " from Array value",
      )
    }
}
