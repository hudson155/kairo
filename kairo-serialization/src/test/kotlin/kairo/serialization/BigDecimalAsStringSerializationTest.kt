package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigDecimalAsStringSerializationTest {
  internal data class DefaultWrapper(
    val value: BigDecimal,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = BigDecimalSerializer.AsString::class)
    @JsonDeserialize(using = BigDecimalDeserializer.AsString::class)
    val value: BigDecimal,
  )

  private val json: KairoJson =
    KairoJson {
      bigDecimalFormat = BigDecimalFormat.String
    }

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      json.serialize(DefaultWrapper(BigDecimal("-90210.0")))
        .shouldBe("""{"value":"-90210.0"}""")
      json.serialize(DefaultWrapper(BigDecimal("-90210")))
        .shouldBe("""{"value":"-90210"}""")
      json.serialize(DefaultWrapper(BigDecimal("-3.14")))
        .shouldBe("""{"value":"-3.14"}""")
      json.serialize(DefaultWrapper(BigDecimal("-0.0")))
        .shouldBe("""{"value":"0.0"}""")
      json.serialize(DefaultWrapper(BigDecimal("0")))
        .shouldBe("""{"value":"0"}""")
      json.serialize(DefaultWrapper(BigDecimal("0.0")))
        .shouldBe("""{"value":"0.0"}""")
      json.serialize(DefaultWrapper(BigDecimal("3.14")))
        .shouldBe("""{"value":"3.14"}""")
      json.serialize(DefaultWrapper(BigDecimal("90210")))
        .shouldBe("""{"value":"90210"}""")
      json.serialize(DefaultWrapper(BigDecimal("90210.0")))
        .shouldBe("""{"value":"90210.0"}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.serialize(WrapperWithAnnotations(BigDecimal("-90210.0")))
        .shouldBe("""{"value":"-90210.0"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-90210")))
        .shouldBe("""{"value":"-90210"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-3.14")))
        .shouldBe("""{"value":"-3.14"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("-0.0")))
        .shouldBe("""{"value":"0.0"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("0")))
        .shouldBe("""{"value":"0"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("0.0")))
        .shouldBe("""{"value":"0.0"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("3.14")))
        .shouldBe("""{"value":"3.14"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("90210")))
        .shouldBe("""{"value":"90210"}""")
      json.serialize(WrapperWithAnnotations(BigDecimal("90210.0")))
        .shouldBe("""{"value":"90210.0"}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      json.deserialize<DefaultWrapper>("""{"value":"-90210.0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-90210.0")))
      json.deserialize<DefaultWrapper>("""{"value":"-90210"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-90210")))
      json.deserialize<DefaultWrapper>("""{"value":"-3.14"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-3.14")))
      json.deserialize<DefaultWrapper>("""{"value":"-1e0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-1")))
      json.deserialize<DefaultWrapper>("""{"value":"-0.0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-0.0")))
      json.deserialize<DefaultWrapper>("""{"value":"-0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("-0")))
      json.deserialize<DefaultWrapper>("""{"value":"0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("0")))
      json.deserialize<DefaultWrapper>("""{"value":"0.0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("0.0")))
      json.deserialize<DefaultWrapper>("""{"value":"1e0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("1")))
      json.deserialize<DefaultWrapper>("""{"value":"3.14"}""")
        .shouldBe(DefaultWrapper(BigDecimal("3.14")))
      json.deserialize<DefaultWrapper>("""{"value":"90210"}""")
        .shouldBe(DefaultWrapper(BigDecimal("90210")))
      json.deserialize<DefaultWrapper>("""{"value":"90210.0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("90210.0")))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.deserialize<WrapperWithAnnotations>("""{"value":"-90210.0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-90210.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-90210"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-90210")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-3.14"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-3.14")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-1e0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-1")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-0.0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-0.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("-0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"0.0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("0.0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"1e0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("1")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"3.14"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("3.14")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"90210"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("90210")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"90210.0"}""")
        .shouldBe(WrapperWithAnnotations(BigDecimal("90210.0")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"9 0210"}""")
      }.message.shouldStartWith(
        "Character   is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )

      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"3.1 4"}""")
      }.message.shouldStartWith(
        "Character   is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<DefaultWrapper>("""{"value":"090210"}""")
        .shouldBe(DefaultWrapper(BigDecimal("90210")))

      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<DefaultWrapper>("""{"value":"03.14"}""")
        .shouldBe(DefaultWrapper(BigDecimal("3.14")))
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"0x0"}""")
      }.message.shouldStartWith(
        "Character x is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"NaN"}""")
      }.message.shouldStartWith(
        "Character N is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"Infinity"}""")
      }.message.shouldStartWith(
        "Character I is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )

      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"-Infinity"}""")
      }.message.shouldStartWith(
        "Character I is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DefaultWrapper>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.BigDecimalAsStringSerializationTest.DefaultWrapper(non-null) but was null",
      )

      json.deserialize<Double?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":true}""")
      }.message.shouldStartWith(
        "Character t is neither a decimal digit number, decimal point, nor \"e\" notation exponential mark.",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<DefaultWrapper>("""{"value":"0"}""")
        .shouldBe(DefaultWrapper(BigDecimal("0")))
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":{}}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":[]}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Array value",
      )
    }
}
