package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LongSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(-4032816692L).shouldBe("-4032816692")
      jsonMapper.writeValueAsString(0L).shouldBe("0")
      jsonMapper.writeValueAsString(4032816692L).shouldBe("4032816692")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Long>("-4032816692").shouldBe(-4032816692L)
      jsonMapper.readValue<Long>("-0").shouldBe(0L)
      jsonMapper.readValue<Long>("0").shouldBe(0L)
      jsonMapper.readValue<Long>("4032816692").shouldBe(4032816692L)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("-9223372036854775809")
      }

      shouldThrowAny {
        jsonMapper.readValue<Long>("9223372036854775808")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("9 876543210")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("04032816692")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("1e0")
      }
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<Long>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("null")
      }

      jsonMapper.readValue<Long?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Long>("""[]""")
      }
    }
}
