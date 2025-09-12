package kairo.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kairo.serialization.kairo
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class IdSerializationTest {
  private val json: Json =
    Json {
      kairo()
    }

  @Test
  fun serialize(): Unit =
    runTest {
      json.encodeToString(UserId("user_2eDS1sMt"))
        .shouldBe("\"user_2eDS1sMt\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.decodeFromString<UserId>("\"user_2eDS1sMt\"")
        .shouldBe(UserId("user_2eDS1sMt"))
      shouldThrow<IllegalArgumentException> {
        json.decodeFromString<UserId>("\"user-2eDS1sMt\"")
      }
    }
}
