package kairo.protectedString

import io.kotest.matchers.shouldBe
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ProtectedString.Access::class)
internal class ProtectedStringSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ProtectedString("1"))
        .shouldBe("\"1\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ProtectedString>("\"1\"")
        .shouldBe(ProtectedString("1"))
    }
}
