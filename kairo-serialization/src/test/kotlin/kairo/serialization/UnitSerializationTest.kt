package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class UnitSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Unit).shouldBe("""{}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Unit>("""{}""").shouldBe(Unit)
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<Unit>("""{"other":"unknown"}""").shouldBe(Unit)
      }.message.shouldStartWith(
        "Unrecognized field \"other\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Unit>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlin.Unit(non-null) but was null",
      )

      json.deserialize<Unit?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Unit>("true")
      }.message.shouldStartWith(
        "Cannot construct instance of `kotlin.Unit`",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Unit>("0")
      }.message.shouldStartWith(
        "Cannot construct instance of `kotlin.Unit`",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Unit>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `kotlin.Unit`" +
          " from Array value",
      )
    }
}
