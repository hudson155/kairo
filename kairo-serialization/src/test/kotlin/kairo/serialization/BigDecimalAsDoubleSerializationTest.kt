package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigDecimalAsDoubleSerializationTest {
  internal data class DefaultWrapper(
    val value: BigDecimal,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = BigDecimalSerializer.AsDouble::class)
    @JsonDeserialize(using = BigDecimalDeserializer.AsDouble::class)
    val value: BigDecimal,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      json.serialize(DefaultWrapper(BigDecimal("-90210.0")))
        .shouldBe("""{"value":-90210.0}""")
      json.serialize(DefaultWrapper(BigDecimal("-90210")))
        .shouldBe("""{"value":-90210}""")
      json.serialize(DefaultWrapper(BigDecimal("-3.14")))
        .shouldBe("""{"value":-3.14}""")
      json.serialize(DefaultWrapper(BigDecimal("-0.0")))
        .shouldBe("""{"value":0.0}""")
      json.serialize(DefaultWrapper(BigDecimal("0")))
        .shouldBe("""{"value":0}""")
      json.serialize(DefaultWrapper(BigDecimal("0.0")))
        .shouldBe("""{"value":0.0}""")
      json.serialize(DefaultWrapper(BigDecimal("3.14")))
        .shouldBe("""{"value":3.14}""")
      json.serialize(DefaultWrapper(BigDecimal("90210")))
        .shouldBe("""{"value":90210}""")
      json.serialize(DefaultWrapper(BigDecimal("90210.0")))
        .shouldBe("""{"value":90210.0}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      json.serialize(WrapperWithAnnotations(BigDecimal("-90210.0")))
        .shouldBe("""{"value":-90210.0}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-90210")))
        .shouldBe("""{"value":-90210}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-3.14")))
        .shouldBe("""{"value":-3.14}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-0.0")))
        .shouldBe("""{"value":0.0}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("0")))
        .shouldBe("""{"value":0}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("0.0")))
        .shouldBe("""{"value":0.0}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("3.14")))
        .shouldBe("""{"value":3.14}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("90210")))
        .shouldBe("""{"value":90210}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("90210.0")))
        .shouldBe("""{"value":90210.0}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      json.deserialize<DefaultWrapper>("""{"value":-90210.0}""")
        .shouldBe(DefaultWrapper(BigDecimal("-90210.0")))
      json.deserialize<DefaultWrapper>("""{"value":-90210}""")
        .shouldBe(DefaultWrapper(BigDecimal("-90210")))
      json.deserialize<DefaultWrapper>("""{"value":-3.14}""")
        .shouldBe(DefaultWrapper(BigDecimal("-3.14")))
      json.deserialize<DefaultWrapper>("""{"value":-1e0}""")
        .shouldBe(DefaultWrapper(BigDecimal("-1")))
      json.deserialize<DefaultWrapper>("""{"value":-0.0}""")
        .shouldBe(DefaultWrapper(BigDecimal("-0.0")))
      json.deserialize<DefaultWrapper>("""{"value":-0}""")
        .shouldBe(DefaultWrapper(BigDecimal("-0")))
      json.deserialize<DefaultWrapper>("""{"value":0}""")
        .shouldBe(DefaultWrapper(BigDecimal("0")))
      json.deserialize<DefaultWrapper>("""{"value":0.0}""")
        .shouldBe(DefaultWrapper(BigDecimal("0.0")))
      json.deserialize<DefaultWrapper>("""{"value":1e0}""")
        .shouldBe(DefaultWrapper(BigDecimal("1")))
      json.deserialize<DefaultWrapper>("""{"value":3.14}""")
        .shouldBe(DefaultWrapper(BigDecimal("3.14")))
      json.deserialize<DefaultWrapper>("""{"value":90210}""")
        .shouldBe(DefaultWrapper(BigDecimal("90210")))
      json.deserialize<DefaultWrapper>("""{"value":90210.0}""")
        .shouldBe(DefaultWrapper(BigDecimal("90210.0")))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      json.deserialize<WrapperWithAnnotations>("""{"value":-90210.0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-90210.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":-90210}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-90210")))
      json.deserialize<WrapperWithAnnotations>("""{"value":-3.14}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-3.14")))
      json.deserialize<WrapperWithAnnotations>("""{"value":-1e0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-1")))
      json.deserialize<WrapperWithAnnotations>("""{"value":-0.0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-0.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":-0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":0.0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("0.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":1e0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("1")))
      json.deserialize<WrapperWithAnnotations>("""{"value":3.14}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("3.14")))
      json.deserialize<WrapperWithAnnotations>("""{"value":90210}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("90210")))
      json.deserialize<WrapperWithAnnotations>("""{"value":90210.0}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("90210.0")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":9 0210}""")
      }.message.shouldStartWith(
        "Unexpected character ('0' (code 48))" +
          ": was expecting comma to separate Object entries",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":3.1 4}""")
      }.message.shouldStartWith(
        "Unexpected character ('4' (code 52))" +
          ": was expecting comma to separate Object entries",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":090210}""")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":03.14}""")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":0x0}""")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": was expecting comma to separate Object entries",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":NaN}""")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":Infinity}""")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":-Infinity}""")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":"0"}""")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.math.BigDecimal` value",
      )
    }
}
