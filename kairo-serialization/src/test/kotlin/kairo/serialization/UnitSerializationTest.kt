package kairo.serialization

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
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
}
