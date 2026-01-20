package kairo.serialization

import io.kotest.matchers.shouldBe
import java.util.UUID
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaUuidKeySerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(mapOf(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))
        .shouldBe("""{"a042df12-b775-42b2-aeb1-5bdd4ea78dc5":"value"}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Map<UUID, String>>("""{"a042df12-b775-42b2-aeb1-5bdd4ea78dc5":"value"}""")
        .shouldBe(mapOf(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))

      json.deserialize<Map<UUID, String>>("""{"A042DF12-B775-42B2-AEB1-5BDD4EA78DC5":"value"}""")
        .shouldBe(mapOf(UUID.fromString("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))
    }
}
