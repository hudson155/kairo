package kairo.protectedString

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  @Test
  fun serialize() {
    Json.encodeToString(ProtectedString("1"))
      .shouldBe("\"1\"")
  }

  @Test
  fun deserialize() {
    Json.decodeFromString<ProtectedString>("\"1\"")
      .shouldBe(ProtectedString("1"))
  }
}
