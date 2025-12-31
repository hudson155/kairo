package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ShortSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString((-155).toShort()).shouldBe("-155")
      jsonMapper.writeValueAsString(0.toShort()).shouldBe("0")
      jsonMapper.writeValueAsString(155.toShort()).shouldBe("155")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Short>("-155").shouldBe((-155).toShort())
      jsonMapper.readValue<Short>("-0").shouldBe(0.toShort())
      jsonMapper.readValue<Short>("0").shouldBe(0.toShort())
      jsonMapper.readValue<Short>("155").shouldBe(155.toShort())
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("-32769")
      }

      shouldThrowAny {
        jsonMapper.readValue<Short>("32768")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("1 55")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("0155")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("1e0")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<Short>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("null")
      }

      jsonMapper.readValue<Short?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Short>("""[]""")
      }
    }
}
