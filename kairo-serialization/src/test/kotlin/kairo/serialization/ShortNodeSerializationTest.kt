package kairo.serialization

import com.fasterxml.jackson.databind.node.ShortNode
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class ShortNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ShortNode.valueOf((-155).toShort()))
        .shouldBe("-155")
      json.serialize(ShortNode.valueOf(0.toShort()))
        .shouldBe("0")
      json.serialize(ShortNode.valueOf(155.toShort()))
        .shouldBe("155")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      // Deserialization uses BigIntegerNode.
    }
}
