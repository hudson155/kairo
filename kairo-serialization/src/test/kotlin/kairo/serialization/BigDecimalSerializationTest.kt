package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigDecimalSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BigDecimal("-90210")).shouldBe("-90210")
      json.serialize(BigDecimal("-3.14")).shouldBe("-3.14")
      json.serialize(BigDecimal("0")).shouldBe("0")
      json.serialize(BigDecimal("3.14")).shouldBe("3.14")
      json.serialize(BigDecimal("90210")).shouldBe("90210")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BigDecimal>("-90210.0").shouldBe(BigDecimal("-90210.0"))
      json.deserialize<BigDecimal>("-90210").shouldBe(BigDecimal("-90210"))
      json.deserialize<BigDecimal>("-3.14").shouldBe(BigDecimal("-3.14"))
      json.deserialize<BigDecimal>("-1e0").shouldBe(BigDecimal("-1e0"))
      json.deserialize<BigDecimal>("-0.0").shouldBe(BigDecimal("-0.0"))
      json.deserialize<BigDecimal>("-0").shouldBe(BigDecimal("-0"))
      json.deserialize<BigDecimal>("0").shouldBe(BigDecimal("0"))
      json.deserialize<BigDecimal>("0.0").shouldBe(BigDecimal("0.0"))
      json.deserialize<BigDecimal>("1e0").shouldBe(BigDecimal("1e0"))
      json.deserialize<BigDecimal>("3.14").shouldBe(BigDecimal("3.14"))
      json.deserialize<BigDecimal>("90210").shouldBe(BigDecimal("90210"))
      json.deserialize<BigDecimal>("90210.0").shouldBe(BigDecimal("90210.0"))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("9 0210")
      }

      shouldThrowAny {
        json.deserialize<BigDecimal>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("090210")
      }

      shouldThrowAny {
        json.deserialize<BigDecimal>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("Infinity")
      }

      shouldThrowAny {
        json.deserialize<BigDecimal>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("null")
      }

      json.deserialize<BigDecimal?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<BigDecimal>("""[]""")
      }
    }
}
