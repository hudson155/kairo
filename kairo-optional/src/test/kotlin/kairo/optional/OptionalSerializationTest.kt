package kairo.optional

import com.fasterxml.jackson.annotation.JsonInclude
import io.kotest.matchers.shouldBe
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class OptionalSerializationTest {
  @JsonInclude(JsonInclude.Include.NON_ABSENT)
  internal data class Wrapper(
    val value: Optional<String>,
  )

  private val json: KairoJson =
    KairoJson {
      addModule(OptionalModule())
    }

  @Test
  fun `serialize, missing`(): Unit =
    runTest {
      json.serialize(Wrapper(Optional.Missing))
        .shouldBe("""{}""")
    }

  @Test
  fun `serialize, present`(): Unit =
    runTest {
      json.serialize(Wrapper(Optional.Value("some value")))
        .shouldBe("""{"value":"some value"}""")
    }

  @Test
  fun `deserialize, missing`(): Unit =
    runTest {
      json.deserialize<Wrapper>("""{}""")
        .shouldBe(Wrapper(Optional.Missing))
    }

  @Test
  fun `deserialize, present`(): Unit =
    runTest {
      json.deserialize<Wrapper>("""{"value":"some value"}""")
        .shouldBe(Wrapper(Optional.Value("some value")))
    }
}
