package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UShortSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(0U.toUShort()).shouldBe("0")
      json.serialize(32923U.toUShort()).shouldBe("32923")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<UShort>("-0").shouldBe(0U.toUShort())
      json.deserialize<UShort>("0").shouldBe(0U.toUShort())
      json.deserialize<UShort>("32923").shouldBe(32923U.toUShort())
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("-1")
      }

      shouldThrowAny {
        json.deserialize<UShort>("65536")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("3 2923")
      }
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("032923")
      }
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      json.deserialize<UShort>("1e0").shouldBe(1U.toUShort())
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("0x0")
      }
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("NaN")
      }
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("Infinity")
      }

      shouldThrowAny {
        json.deserialize<UShort>("-Infinity")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("null")
      }

      json.deserialize<UShort?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      /**
       * This seems like a bug in Jackson!
       */
      json.deserialize<UShort>("0.0").shouldBe(0U.toUShort())
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("\"0\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<UShort>("""[]""")
      }
    }
}
