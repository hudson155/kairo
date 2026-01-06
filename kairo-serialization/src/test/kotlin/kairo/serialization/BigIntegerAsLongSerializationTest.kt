package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerAsLongSerializationTest {
  internal data class DefaultWrapper(
    val value: BigInteger,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = BigIntegerSerializer.AsLong::class)
    @JsonDeserialize(using = BigIntegerDeserializer.AsLong::class)
    val value: BigInteger,
  )

  private val json: KairoJson =
    KairoJson {
      bigIntegerFormat = BigIntegerFormat.Long
    }

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      json.serialize(DefaultWrapper(BigInteger("-1234567890987654321")))
        .shouldBe("""{"value":-1234567890987654321}""")
      json.serialize(DefaultWrapper(BigInteger("0")))
        .shouldBe("""{"value":0}""")
      json.serialize(DefaultWrapper(BigInteger("1234567890987654321")))
        .shouldBe("""{"value":1234567890987654321}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.serialize(WrapperWithAnnotations(BigInteger("-1234567890987654321")))
        .shouldBe("""{"value":-1234567890987654321}""")
      json.serialize(WrapperWithAnnotations(BigInteger("0")))
        .shouldBe("""{"value":0}""")
      json.serialize(WrapperWithAnnotations(BigInteger("1234567890987654321")))
        .shouldBe("""{"value":1234567890987654321}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      json.deserialize<DefaultWrapper>("""{"value":-1234567890987654321}""")
        .shouldBe(DefaultWrapper(BigInteger("-1234567890987654321")))
      json.deserialize<DefaultWrapper>("""{"value":0}""")
        .shouldBe(DefaultWrapper(BigInteger("0")))
      json.deserialize<DefaultWrapper>("""{"value":0}""")
        .shouldBe(DefaultWrapper(BigInteger("0")))
      json.deserialize<DefaultWrapper>("""{"value":1234567890987654321}""")
        .shouldBe(DefaultWrapper(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.deserialize<WrapperWithAnnotations>("""{"value":-1234567890987654321}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("-1234567890987654321")))
      json.deserialize<WrapperWithAnnotations>("""{"value":0}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":0}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":1234567890987654321}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":1 234567890987654321}""")
      }.message.shouldStartWith(
        "Unexpected character ('2' (code 50))" +
          ": was expecting comma to separate Object entries",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DefaultWrapper>("""{"value":01234567890987654321}""")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DefaultWrapper>("""{"value":1e0}""")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (1e0)" +
          " to `java.math.BigInteger` value",
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
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DefaultWrapper>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.BigIntegerAsLongSerializationTest.DefaultWrapper(non-null) but was null",
      )

      json.deserialize<DefaultWrapper?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":true}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DefaultWrapper>("""{"value":0.0}""")
      }.message.shouldStartWith(
        "Cannot coerce Floating-point value (0.0)" +
          " to `java.math.BigInteger` value",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":"0"}""")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"0\")" +
          " to `java.math.BigInteger` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":{}}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DefaultWrapper>("""{"value":[]}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.math.BigInteger`" +
          " from Array value",
      )
    }
}
