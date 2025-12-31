package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class StringSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString("").shouldBe("\"\"")
      jsonMapper.writeValueAsString("Hello, World!").shouldBe("\"Hello, World!\"")
      jsonMapper.writeValueAsString("✝\uFE0F").shouldBe("\"✝\uFE0F\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<String>("\"\"").shouldBe("")
      jsonMapper.readValue<String>("\"Hello, World!\"").shouldBe("Hello, World!")
      jsonMapper.readValue<String>("\"✝\uFE0F\"").shouldBe("✝\uFE0F")
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<String>("null")
      }

      jsonMapper.readValue<String?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      jsonMapper.readValue<String>("true").shouldBe("true")
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      jsonMapper.readValue<String>("0").shouldBe("0")
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      jsonMapper.readValue<String>("0.0").shouldBe("0.0")
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<String>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<String>("""[]""")
      }
    }
}
