package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class CharArraySerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize("".toCharArray()).shouldBe("\"\"")
      json.serialize("Hello, World!".toCharArray()).shouldBe("\"Hello, World!\"")
      json.serialize("✝\uFE0F".toCharArray()).shouldBe("\"✝\uFE0F\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<CharArray>("\"\"").shouldBe("".toCharArray())
      json.deserialize<CharArray>("\"Hello, World!\"").shouldBe("Hello, World!".toCharArray())
      json.deserialize<CharArray>("\"✝\uFE0F\"").shouldBe("✝\uFE0F".toCharArray())

      json.deserialize<CharArray>(
        """[]""",
      ).shouldBe("".toCharArray())
      json.deserialize<CharArray>(
        """["H","e","l","l","o",","," ","W","o","r","l","d","!"]""",
      ).shouldBe("Hello, World!".toCharArray())
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<CharArray>("null")
      }

      json.deserialize<CharArray?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<CharArray>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<CharArray>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<CharArray>("""{}""")
      }
    }
}
