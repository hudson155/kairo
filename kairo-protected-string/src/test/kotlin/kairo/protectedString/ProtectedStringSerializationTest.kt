package kairo.protectedString

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ProtectedString("Hello, World!"))
        .shouldBe("\"Hello, World!\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ProtectedString>("\"Hello, World!\"")
        .shouldBe(ProtectedString("Hello, World!"))
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ProtectedString>("""{}""")
      }.message.shouldStartWith(
        "Cannot construct instance of `kairo.protectedString.ProtectedString`",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<ProtectedString>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `kairo.protectedString.ProtectedString`" +
          " from Array value",
      )
    }
}
