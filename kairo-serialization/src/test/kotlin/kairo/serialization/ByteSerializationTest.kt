package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.core.exc.InputCoercionException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ByteSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize((-42).toByte()).shouldBe("-42")
      json.serialize(0.toByte()).shouldBe("0")
      json.serialize(42.toByte()).shouldBe("42")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Byte>("-42").shouldBe((-42).toByte())
      json.deserialize<Byte>("-0").shouldBe(0.toByte())
      json.deserialize<Byte>("0").shouldBe(0.toByte())
      json.deserialize<Byte>("42").shouldBe(42.toByte())
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<Byte>("-129")
      }

      /**
       * This seems like a bug in Jackson!
       */
      json.deserialize<Byte>("128").shouldBe((-128).toByte())
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Byte>("4 2")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Byte>("042")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Byte>("1e0")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Byte>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Byte>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Byte>("Infinity")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Byte>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Byte>("null")
      }

      json.deserialize<Byte?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Byte>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Byte>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Byte>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Byte>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Byte>("""[]""")
      }
    }
}
