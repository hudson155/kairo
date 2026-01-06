package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
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
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Char>("\"\"")
      }.message.shouldStartWith(
        "Cannot coerce empty String (\"\")" +
          " to `java.lang.Character` value",
      )
    }

  @Test
  fun `deserialize, wrong format (too long)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Char>("\"Hello, World!\"")
      }.message.shouldStartWith(
        "Cannot coerce String value (\"Hello, World!\")" +
          " to `java.lang.Character` value",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Char>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Char(non-null)" +
          " but was null",
      )

      json.deserialize<Char?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Char>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Character`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Char>("0")
      }.message.shouldStartWith(
        "Cannot coerce Integer value (0)" +
          " to `java.lang.Character` value",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Char>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Character`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Char>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.Character`" +
          " from Array value",
      )
    }
}
