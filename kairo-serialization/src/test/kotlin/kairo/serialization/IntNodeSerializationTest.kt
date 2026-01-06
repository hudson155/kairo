package kairo.serialization

import com.fasterxml.jackson.databind.node.IntNode
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class IntNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(IntNode.valueOf(-90210))
        .shouldBe("-90210")
      json.serialize(IntNode.valueOf(0))
        .shouldBe("0")
      json.serialize(IntNode.valueOf(90210))
        .shouldBe("90210")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      // Deserialization uses BigIntegerNode.
    }
}
