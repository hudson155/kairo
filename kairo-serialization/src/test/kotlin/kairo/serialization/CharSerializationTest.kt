package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CharSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString('x').shouldBe("\"x\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Char>("\"x\"").shouldBe('x')
    }

  @Test
  fun `deserialize, wrong format (empty)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("")
      }
    }

  @Test
  fun `deserialize, wrong format (too long)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("\"Hello, World!\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("null")
      }

      jsonMapper.readValue<Char?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Char>("""[]""")
      }
    }
}
