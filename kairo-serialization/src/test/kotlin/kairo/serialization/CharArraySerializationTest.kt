package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CharArraySerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString("".toCharArray()).shouldBe("\"\"")
      jsonMapper.writeValueAsString("Hello, World!".toCharArray()).shouldBe("\"Hello, World!\"")
      jsonMapper.writeValueAsString("✝\uFE0F".toCharArray()).shouldBe("\"✝\uFE0F\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<CharArray>("\"\"").shouldBe("".toCharArray())
      jsonMapper.readValue<CharArray>("\"Hello, World!\"").shouldBe("Hello, World!".toCharArray())
      jsonMapper.readValue<CharArray>("\"✝\uFE0F\"").shouldBe("✝\uFE0F".toCharArray())

      jsonMapper.readValue<CharArray>(
        """[]"""
      ).shouldBe("".toCharArray())
      jsonMapper.readValue<CharArray>(
        """["H","e","l","l","o",","," ","W","o","r","l","d","!"]"""
      ).shouldBe("Hello, World!".toCharArray())
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<CharArray>("null")
      }

      jsonMapper.readValue<CharArray?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<CharArray>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<CharArray>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<CharArray>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<CharArray>("""{}""")
      }
    }
}
