package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class DoubleSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize((-90210).toDouble()).shouldBe("-90210.0")
      json.serialize(-3.14).shouldBe("-3.14")
      json.serialize(0.toDouble()).shouldBe("0.0")
      json.serialize(3.14).shouldBe("3.14")
      json.serialize(90210.toDouble()).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Double>("-90210.0").shouldBe((-90210).toDouble())
      json.deserialize<Double>("-90210").shouldBe((-90210).toDouble())
      json.deserialize<Double>("-3.14").shouldBe(-3.14)
      json.deserialize<Double>("-1e0").shouldBe((-1).toDouble())
      json.deserialize<Double>("-0.0").shouldBe(0.toDouble())
      json.deserialize<Double>("-0").shouldBe(0.toDouble())
      json.deserialize<Double>("0").shouldBe(0.toDouble())
      json.deserialize<Double>("0.0").shouldBe(0.toDouble())
      json.deserialize<Double>("1e0").shouldBe(1.toDouble())
      json.deserialize<Double>("3.14").shouldBe(3.14)
      json.deserialize<Double>("90210").shouldBe(90210.toDouble())
      json.deserialize<Double>("90210.0").shouldBe(90210.toDouble())
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("9 0210")
      }

      shouldThrowAny {
        json.deserialize<Double>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("090210")
      }

      shouldThrowAny {
        json.deserialize<Double>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("Infinity")
      }

      shouldThrowAny {
        json.deserialize<Double>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("null")
      }

      json.deserialize<Double?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Double>("""[]""")
      }
    }
}
