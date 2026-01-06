package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigDecimalSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BigDecimal("-90210.0")).shouldBe("-90210.0")
      json.serialize(BigDecimal("-90210")).shouldBe("-90210")
      json.serialize(BigDecimal("-3.14")).shouldBe("-3.14")
      json.serialize(BigDecimal("-0.0")).shouldBe("0.0")
      json.serialize(BigDecimal("0")).shouldBe("0")
      json.serialize(BigDecimal("0.0")).shouldBe("0.0")
      json.serialize(BigDecimal("3.14")).shouldBe("3.14")
      json.serialize(BigDecimal("90210")).shouldBe("90210")
      json.serialize(BigDecimal("90210.0")).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BigDecimal>("-90210.0").shouldBe(BigDecimal("-90210.0"))
      json.deserialize<BigDecimal>("-90210").shouldBe(BigDecimal("-90210"))
      json.deserialize<BigDecimal>("-3.14").shouldBe(BigDecimal("-3.14"))
      json.deserialize<BigDecimal>("-1e0").shouldBe(BigDecimal("-1"))
      json.deserialize<BigDecimal>("-0.0").shouldBe(BigDecimal("-0.0"))
      json.deserialize<BigDecimal>("-0").shouldBe(BigDecimal("-0"))
      json.deserialize<BigDecimal>("0").shouldBe(BigDecimal("0"))
      json.deserialize<BigDecimal>("0.0").shouldBe(BigDecimal("0.0"))
      json.deserialize<BigDecimal>("1e0").shouldBe(BigDecimal("1"))
      json.deserialize<BigDecimal>("3.14").shouldBe(BigDecimal("3.14"))
      json.deserialize<BigDecimal>("90210").shouldBe(BigDecimal("90210"))
      json.deserialize<BigDecimal>("90210.0").shouldBe(BigDecimal("90210.0"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("9 0210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigDecimal>("3.1 4")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `java.math.BigDecimal`)"
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("090210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("03.14")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed"
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values"
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'"
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'"
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigDecimal>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'"
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigDecimal>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.math.BigDecimal(non-null) but was null"
      )

      json.deserialize<BigDecimal?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigDecimal>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigDecimal`" +
          " from Boolean value"
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigDecimal>("\"0\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.math.BigDecimal` value"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigDecimal>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigDecimal`" +
          " from Object value"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigDecimal>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigDecimal`" +
          " from Array value"
      )
    }
}
