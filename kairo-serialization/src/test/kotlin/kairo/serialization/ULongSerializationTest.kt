package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.exc.InputCoercionException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ULongSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(0UL).shouldBe("0")
      json.serialize(9223372046731319018UL).shouldBe("9223372046731319018")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ULong>("-0").shouldBe(0UL)
      json.deserialize<ULong>("0").shouldBe(0UL)
      json.deserialize<ULong>("9223372046731319018").shouldBe(9223372046731319018UL)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowExactly<InputCoercionException> {
        json.deserialize<ULong>("-1")
      }

      shouldThrowExactly<InputCoercionException> {
        json.deserialize<ULong>("18446744073709551616")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ULong>("9 223372046731319018")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("09223372046731319018")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      json.deserialize<ULong>("1e0").shouldBe(1UL)
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("Infinity")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<ULong>("null")
      }

      json.deserialize<ULong?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      json.deserialize<ULong>("0.0").shouldBe(0UL)
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<ULong>("""[]""")
      }
    }
}
