package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigDecimalSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(BigDecimal("-90210")).shouldBe("-90210")
      jsonMapper.writeValueAsString(BigDecimal("-3.14")).shouldBe("-3.14")
      jsonMapper.writeValueAsString(BigDecimal("0")).shouldBe("0")
      jsonMapper.writeValueAsString(BigDecimal("3.14")).shouldBe("3.14")
      jsonMapper.writeValueAsString(BigDecimal("90210")).shouldBe("90210")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<BigDecimal>("-90210.0").shouldBe(BigDecimal("-90210.0"))
      jsonMapper.readValue<BigDecimal>("-90210").shouldBe(BigDecimal("-90210"))
      jsonMapper.readValue<BigDecimal>("-3.14").shouldBe(BigDecimal("-3.14"))
      jsonMapper.readValue<BigDecimal>("-1e0").shouldBe(BigDecimal("-1e0"))
      jsonMapper.readValue<BigDecimal>("-0.0").shouldBe(BigDecimal("-0.0"))
      jsonMapper.readValue<BigDecimal>("-0").shouldBe(BigDecimal("-0"))
      jsonMapper.readValue<BigDecimal>("0").shouldBe(BigDecimal("0"))
      jsonMapper.readValue<BigDecimal>("0.0").shouldBe(BigDecimal("0.0"))
      jsonMapper.readValue<BigDecimal>("1e0").shouldBe(BigDecimal("1e0"))
      jsonMapper.readValue<BigDecimal>("3.14").shouldBe(BigDecimal("3.14"))
      jsonMapper.readValue<BigDecimal>("90210").shouldBe(BigDecimal("90210"))
      jsonMapper.readValue<BigDecimal>("90210.0").shouldBe(BigDecimal("90210.0"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("9 0210")
      }

      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("090210")
      }

      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("null")
      }

      jsonMapper.readValue<BigDecimal?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<BigDecimal>("""[]""")
      }
    }
}
