package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KotlinUuidSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))
        .shouldBe("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Uuid>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5\"")
        .shouldBe(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))

      json.deserialize<Uuid>("\"A042DF12-B775-42B2-AEB1-5BDD4EA78DC5\"")
        .shouldBe(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))

      json.deserialize<Uuid>("\"a042df12b77542b2aeb15bdd4ea78dc5\"")
        .shouldBe(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5"))
    }

  @Test
  fun `deserialize, wrong format (too short)`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        json.deserialize<Uuid>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc\"")
      }.message.shouldStartWith(
        "Expected either a 36-char string in the standard hex-and-dash UUID format or a 32-char hexadecimal string",
      )
    }

  @Test
  fun `deserialize, wrong format (too long)`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        json.deserialize<Uuid>("\"a042df12-b775-42b2-aeb1-5bdd4ea78dc5c\"")
      }.message.shouldStartWith(
        "Expected either a 36-char string in the standard hex-and-dash UUID format or a 32-char hexadecimal string",
      )
    }
}
