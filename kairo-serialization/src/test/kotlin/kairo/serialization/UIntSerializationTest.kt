package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UIntSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(0U).shouldBe("0")
      jsonMapper.writeValueAsString(2147573858U).shouldBe("2147573858")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<UInt>("-0").shouldBe(0U)
      jsonMapper.readValue<UInt>("0").shouldBe(0U)
      jsonMapper.readValue<UInt>("2147573858").shouldBe(2147573858U)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("-1")
      }

      shouldThrowAny {
        jsonMapper.readValue<UInt>("4294967296")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("2 147573858")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("02147573858")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      jsonMapper.readValue<UInt>("1e0").shouldBe(1U)
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<UInt>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("null")
      }

      jsonMapper.readValue<UInt?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      jsonMapper.readValue<UInt>("0.0").shouldBe(0U)
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<UInt>("""[]""")
      }
    }
}
