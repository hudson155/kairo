package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ULongSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(0UL).shouldBe("0")
      jsonMapper.writeValueAsString(9223372046731319018UL).shouldBe("9223372046731319018")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<ULong>("-0").shouldBe(0UL)
      jsonMapper.readValue<ULong>("0").shouldBe(0UL)
      jsonMapper.readValue<ULong>("9223372046731319018").shouldBe(9223372046731319018UL)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("-1")
      }

      shouldThrowAny {
        jsonMapper.readValue<ULong>("18446744073709551616")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("9 223372046731319018")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("09223372046731319018")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      jsonMapper.readValue<ULong>("1e0").shouldBe(1UL)
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("Infinity")
      }

      shouldThrowAny {
        jsonMapper.readValue<ULong>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("null")
      }

      jsonMapper.readValue<ULong?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      jsonMapper.readValue<ULong>("0.0").shouldBe(0UL)
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ULong>("""[]""")
      }
    }
}
