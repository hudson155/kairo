package kairo.id

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class IdSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(UserId("user_2eDS1sMt"))
        .shouldBe("\"user_2eDS1sMt\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<UserId>("\"user_2eDS1sMt\"")
        .shouldBe(UserId("user_2eDS1sMt"))

      shouldThrowExactly<IllegalArgumentException> {
        json.deserialize<UserId>("\"user-2eDS1sMt\"")
      }.shouldHaveMessage("Malformed user ID (value=user-2eDS1sMt).")
    }
}
