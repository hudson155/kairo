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
internal class DoubleSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize((-90210).toDouble()).shouldBe("-90210.0")
      json.serialize(-3.14).shouldBe("-3.14")
      json.serialize(0.toDouble()).shouldBe("0.0")
      json.serialize(3.14).shouldBe("3.14")
      json.serialize(90210.toDouble()).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Double>("-90210.0").shouldBe((-90210).toDouble())
      json.deserialize<Double>("-90210").shouldBe((-90210).toDouble())
      json.deserialize<Double>("-3.14").shouldBe(-3.14)
      json.deserialize<Double>("-1e0").shouldBe((-1).toDouble())
      json.deserialize<Double>("-0.0").shouldBe(0.toDouble())
      json.deserialize<Double>("-0").shouldBe(0.toDouble())
      json.deserialize<Double>("0").shouldBe(0.toDouble())
      json.deserialize<Double>("0.0").shouldBe(0.toDouble())
      json.deserialize<Double>("1e0").shouldBe(1.toDouble())
      json.deserialize<Double>("3.14").shouldBe(3.14)
      json.deserialize<Double>("90210").shouldBe(90210.toDouble())
      json.deserialize<Double>("90210.0").shouldBe(90210.toDouble())
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("9 0210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Double>("3.1 4")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.lang.Double`)"
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("090210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("03.14")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values"
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'"
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'"
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Double>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'"
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Double>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Double(non-null) but was null"
      )

      json.deserialize<Double?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Double>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Double`" +
          " from Boolean value"
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Double>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.lang.Double` value"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Double>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Double`" +
          " from Object value"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Double>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Double`" +
          " from Array value"
      )
    }
}
