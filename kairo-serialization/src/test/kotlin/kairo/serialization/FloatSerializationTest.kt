package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
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
      }

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("090210")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("Infinity")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Float>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Float>("null")
      }

      json.deserialize<Float?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Float>("""[]""")
      }
    }
}
