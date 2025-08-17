package kairo.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class IdSerializationTest {
  @Test
  fun serialize(): Unit =
    runTest {
      Json.encodeToString(Id.parse("user_2eDS1sMt"))
        .shouldBe("\"user_2eDS1sMt\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      Json.decodeFromString<Id>("\"user_2eDS1sMt\"")
        .shouldBe(Id.parse("user_2eDS1sMt"))
      shouldThrow<IllegalArgumentException> {
        Json.decodeFromString<Id>("\"user-2eDS1sMt\"")
      }
    }
}
