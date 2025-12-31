package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DoubleSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString((-90210).toDouble()).shouldBe("-90210.0")
      jsonMapper.writeValueAsString(-3.14).shouldBe("-3.14")
      jsonMapper.writeValueAsString(0.toDouble()).shouldBe("0.0")
      jsonMapper.writeValueAsString(3.14).shouldBe("3.14")
      jsonMapper.writeValueAsString(90210.toDouble()).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Double>("-90210.0").shouldBe((-90210).toDouble())
      jsonMapper.readValue<Double>("-90210").shouldBe((-90210).toDouble())
      jsonMapper.readValue<Double>("-3.14").shouldBe(-3.14)
      jsonMapper.readValue<Double>("-1e0").shouldBe((-1).toDouble())
      jsonMapper.readValue<Double>("-0.0").shouldBe(0.toDouble())
      jsonMapper.readValue<Double>("-0").shouldBe(0.toDouble())
      jsonMapper.readValue<Double>("0").shouldBe(0.toDouble())
      jsonMapper.readValue<Double>("0.0").shouldBe(0.toDouble())
      jsonMapper.readValue<Double>("1e0").shouldBe(1.toDouble())
      jsonMapper.readValue<Double>("3.14").shouldBe(3.14)
      jsonMapper.readValue<Double>("90210").shouldBe(90210.toDouble())
      jsonMapper.readValue<Double>("90210.0").shouldBe(90210.toDouble())
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("9 0210")
      }

      shouldThrowAny {
        jsonMapper.readValue<Double>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("090210")
      }

      shouldThrowAny {
        jsonMapper.readValue<Double>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<Double>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("null")
      }

      jsonMapper.readValue<Double?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Double>("""[]""")
      }
    }
}
