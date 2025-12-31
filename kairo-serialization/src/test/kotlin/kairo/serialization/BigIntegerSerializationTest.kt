package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(BigInteger("-1234567890987654321")).shouldBe("-1234567890987654321")
      jsonMapper.writeValueAsString(BigInteger("0")).shouldBe("0")
      jsonMapper.writeValueAsString(BigInteger("1234567890987654321")).shouldBe("1234567890987654321")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<BigInteger>("-1234567890987654321").shouldBe(BigInteger("-1234567890987654321"))
      jsonMapper.readValue<BigInteger>("-0").shouldBe(BigInteger("0"))
      jsonMapper.readValue<BigInteger>("0").shouldBe(BigInteger("0"))
      jsonMapper.readValue<BigInteger>("1234567890987654321").shouldBe(BigInteger("1234567890987654321"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("1 234567890987654321")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("01234567890987654321")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("1e0")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("null")
      }

      jsonMapper.readValue<BigInteger?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigInteger>("""[]""")
      }
    }
}
