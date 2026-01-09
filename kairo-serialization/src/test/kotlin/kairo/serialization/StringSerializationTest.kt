package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
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
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      // Type coercion occurs.
      json.deserialize<String>("true").shouldBe("true")
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      // Type coercion occurs.
      json.deserialize<String>("0").shouldBe("0")
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<String>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<String>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Array value",
      )
    }
}
