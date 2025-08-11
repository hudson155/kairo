package kairo.protectedString

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  @Test
  fun serialize() {
    runTest {
      Json.encodeToString(ProtectedString("1"))
        .shouldBe("\"1\"")
    }
  }

  @Test
  fun deserialize() {
    runTest {
      Json.decodeFromString<ProtectedString>("\"1\"")
        .shouldBe(ProtectedString("1"))
    }
  }
}
