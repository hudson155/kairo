package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CharSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize('x').shouldBe("\"x\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Char>("\"x\"").shouldBe('x')
    }

  @Test
  fun `deserialize, wrong format (empty)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("")
      }
    }

  @Test
  fun `deserialize, wrong format (too long)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("\"Hello, World!\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("null")
      }

      json.deserialize<Char?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Char>("""[]""")
      }
    }
}
