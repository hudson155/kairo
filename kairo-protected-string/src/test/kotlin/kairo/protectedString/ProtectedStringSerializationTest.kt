package kairo.protectedString

import io.kotest.matchers.shouldBe
import kairo.serialization.kairo
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  private val json: Json =
    Json {
      kairo()
    }

  @Test
  fun serialize(): Unit =
    runTest {
      json.encodeToString(ProtectedString("1"))
        .shouldBe("\"1\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.decodeFromString<ProtectedString>("\"1\"")
        .shouldBe(ProtectedString("1"))
    }
}
