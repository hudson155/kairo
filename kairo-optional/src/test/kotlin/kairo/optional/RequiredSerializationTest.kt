package kairo.optional

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RequiredSerializationTest {
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  internal data class Wrapper(
    val value: Required<String>,
  )

  private val json: KairoJson =
    KairoJson {
      addModule(OptionalModule())
    }

  @Test
  fun `serialize, missing`(): Unit =
    runTest {
      json.serialize(Wrapper(Required.Missing))
        .shouldBe("""{}""")
    }

  @Test
  fun `serialize, present`(): Unit =
    runTest {
      json.serialize(Wrapper(Required.Value("some value")))
        .shouldBe("""{"value":"some value"}""")
    }

  @Test
  fun `deserialize, missing`(): Unit =
    runTest {
      json.deserialize<Wrapper>("""{}""")
        .shouldBe(Wrapper(Required.Missing))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Wrapper>("""{"value":null}""")
      }.message.shouldStartWith(
        "Null value for creator property 'value' (index 0)",
      )
    }

  @Test
  fun `deserialize, present`(): Unit =
    runTest {
      json.deserialize<Wrapper>("""{"value":"some value"}""")
        .shouldBe(Wrapper(Required.Value("some value")))
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<String>("""{"value":{}}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Wrapper>("""{"value":[]}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.lang.String`" +
          " from Array value",
      )
    }
}
