package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class StringSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize("").shouldBe("\"\"")
      json.serialize("Hello, World!").shouldBe("\"Hello, World!\"")
      json.serialize("✝\uFE0F").shouldBe("\"✝\uFE0F\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<String>("\"\"").shouldBe("")
      json.deserialize<String>("\"Hello, World!\"").shouldBe("Hello, World!")
      json.deserialize<String>("\"✝\uFE0F\"").shouldBe("✝\uFE0F")
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<String>("null")
      }

      json.deserialize<String?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      json.deserialize<String>("true").shouldBe("true")
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      json.deserialize<String>("0").shouldBe("0")
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      json.deserialize<String>("0.0").shouldBe("0.0")
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<String>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<String>("""[]""")
      }
    }
}
