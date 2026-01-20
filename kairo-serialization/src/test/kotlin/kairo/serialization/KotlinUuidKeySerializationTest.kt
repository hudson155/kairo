package kairo.serialization

import io.kotest.matchers.shouldBe
import kotlin.uuid.Uuid
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KotlinUuidKeySerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(mapOf(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))
        .shouldBe("""{"a042df12-b775-42b2-aeb1-5bdd4ea78dc5":"value"}""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Map<Uuid, String>>("""{"a042df12-b775-42b2-aeb1-5bdd4ea78dc5":"value"}""")
        .shouldBe(mapOf(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))

      json.deserialize<Map<Uuid, String>>("""{"A042DF12-B775-42B2-AEB1-5BDD4EA78DC5":"value"}""")
        .shouldBe(mapOf(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))

      json.deserialize<Map<Uuid, String>>("""{"a042df12b77542b2aeb15bdd4ea78dc5":"value"}""")
        .shouldBe(mapOf(Uuid.parse("a042df12-b775-42b2-aeb1-5bdd4ea78dc5") to "value"))
    }
}
