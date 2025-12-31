package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class FloatSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(-90210F).shouldBe("-90210.0")
      jsonMapper.writeValueAsString(-3.14F).shouldBe("-3.14")
      jsonMapper.writeValueAsString(0F).shouldBe("0.0")
      jsonMapper.writeValueAsString(3.14F).shouldBe("3.14")
      jsonMapper.writeValueAsString(90210F).shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Float>("-90210.0").shouldBe(-90210F)
      jsonMapper.readValue<Float>("-90210").shouldBe(-90210F)
      jsonMapper.readValue<Float>("-3.14").shouldBe(-3.14F)
      jsonMapper.readValue<Float>("-1e0").shouldBe(-1F)
      jsonMapper.readValue<Float>("-0.0").shouldBe(0F)
      jsonMapper.readValue<Float>("-0").shouldBe(0F)
      jsonMapper.readValue<Float>("0").shouldBe(0F)
      jsonMapper.readValue<Float>("0.0").shouldBe(0F)
      jsonMapper.readValue<Float>("1e0").shouldBe(1F)
      jsonMapper.readValue<Float>("3.14").shouldBe(3.14F)
      jsonMapper.readValue<Float>("90210").shouldBe(90210F)
      jsonMapper.readValue<Float>("90210.0").shouldBe(90210F)
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("9 0210")
      }

      shouldThrowAny {
        jsonMapper.readValue<Float>("3.1 4")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("090210")
      }

      shouldThrowAny {
        jsonMapper.readValue<Float>("03.14")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<Float>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("null")
      }

      jsonMapper.readValue<Float?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Float>("""[]""")
      }
    }
}
