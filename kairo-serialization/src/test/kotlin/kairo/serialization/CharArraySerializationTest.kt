package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
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
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<CharArray>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.CharArray(non-null) but was null",
      )

      json.deserialize<CharArray?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<CharArray>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `char[]`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<CharArray>("0")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `char[]`" +
          " from Integer value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<CharArray>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `char[]`" +
          " from Object value",
      )
    }
}
