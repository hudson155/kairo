package kairo.serialization

import com.fasterxml.jackson.databind.node.LongNode
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class LongNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LongNode.valueOf(-4032816692L))
        .shouldBe("-4032816692")
      json.serialize(LongNode.valueOf(0))
        .shouldBe("0")
      json.serialize(LongNode.valueOf(4032816692L))
        .shouldBe("4032816692")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      // Deserialization uses BigIntegerNode.
    }
}
