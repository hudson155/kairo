package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BigInteger("-1234567890987654321")).shouldBe("-1234567890987654321")
      json.serialize(BigInteger("0")).shouldBe("0")
      json.serialize(BigInteger("1234567890987654321")).shouldBe("1234567890987654321")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BigInteger>("-1234567890987654321").shouldBe(BigInteger("-1234567890987654321"))
      json.deserialize<BigInteger>("-0").shouldBe(BigInteger("0"))
      json.deserialize<BigInteger>("0").shouldBe(BigInteger("0"))
      json.deserialize<BigInteger>("1234567890987654321").shouldBe(BigInteger("1234567890987654321"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("1 234567890987654321")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("01234567890987654321")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("1e0")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("Infinity")
      }

      shouldThrowAny {
        json.deserialize<BigInteger>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("null")
      }

      json.deserialize<BigInteger?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigInteger>("""[]""")
      }
    }
}
