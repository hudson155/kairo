package kairo.serialization

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerAsStringSerializationTest {
  internal data class DefaultWrapper(
    val value: BigInteger,
  )

  internal data class WrapperWithAnnotations(
    @JsonSerialize(using = BigIntegerSerializer.AsString::class)
    @JsonDeserialize(using = BigIntegerDeserializer.AsString::class)
    val value: BigInteger,
  )

  private val json: KairoJson =
    KairoJson {
      bigIntegerFormat = BigIntegerFormat.String
    }

  @Test
  fun `serialize, configured in constructor`(): Unit =
    runTest {
      json.serialize(DefaultWrapper(BigInteger("-1234567890987654321")))
        .shouldBe("""{"value":"-1234567890987654321"}""")
      json.serialize(DefaultWrapper(BigInteger("0")))
        .shouldBe("""{"value":"0"}""")
      json.serialize(DefaultWrapper(BigInteger("1234567890987654321")))
        .shouldBe("""{"value":"1234567890987654321"}""")
    }

  @Test
  fun `serialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.serialize(WrapperWithAnnotations(BigInteger("-1234567890987654321")))
        .shouldBe("""{"value":"-1234567890987654321"}""")
      json.serialize(WrapperWithAnnotations(BigInteger("0")))
        .shouldBe("""{"value":"0"}""")
      json.serialize(WrapperWithAnnotations(BigInteger("1234567890987654321")))
        .shouldBe("""{"value":"1234567890987654321"}""")
    }

  @Test
  fun `deserialize, configured in constructor`(): Unit =
    runTest {
      json.deserialize<DefaultWrapper>("""{"value":"-1234567890987654321"}""")
        .shouldBe(DefaultWrapper(BigInteger("-1234567890987654321")))
      json.deserialize<DefaultWrapper>("""{"value":"-0"}""")
        .shouldBe(DefaultWrapper(BigInteger("0")))
      json.deserialize<DefaultWrapper>("""{"value":"0"}""")
        .shouldBe(DefaultWrapper(BigInteger("0")))
      json.deserialize<DefaultWrapper>("""{"value":"1234567890987654321"}""")
        .shouldBe(DefaultWrapper(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, configured using annotations`(): Unit =
    runTest {
      val json = KairoJson()

      json.deserialize<WrapperWithAnnotations>("""{"value":"-1234567890987654321"}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("-1234567890987654321")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"-0"}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"0"}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("0")))
      json.deserialize<WrapperWithAnnotations>("""{"value":"1234567890987654321"}""")
        .shouldBe(WrapperWithAnnotations(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"1 234567890987654321"}""")
      }.message.shouldStartWith(
        "For input string: \"1 \"",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<DefaultWrapper>("""{"value":"01234567890987654321"}""")
        .shouldBe(DefaultWrapper(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"1e0"}""")
      }.message.shouldStartWith(
        "For input string: \"1e0\"",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"0x0"}""")
      }.message.shouldStartWith(
        "For input string: \"x0\"",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"NaN"}""")
      }.message.shouldStartWith(
        "For input string: \"NaN\"",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"Infinity"}""")
      }.message.shouldStartWith(
        "For input string: \"Infinity\"",
      )

      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":"-Infinity"}""")
      }.message.shouldStartWith(
        "For input string: \"Infinity\"",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      // This seems like a bug in Jackson! I think this should throw.
      json.deserialize<DefaultWrapper>("""{"value":0}""")
        .shouldBe(DefaultWrapper(BigInteger("0")))
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<JsonMappingException> {
        json.deserialize<DefaultWrapper>("""{"value":0.0}""")
      }.message.shouldStartWith(
        "For input string: \".0\"",
      )
    }
}
