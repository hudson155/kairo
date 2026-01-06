package kairo.serialization

import com.fasterxml.jackson.databind.node.DoubleNode
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class DoubleNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DoubleNode.valueOf((-90210).toDouble()))
        .shouldBe("-90210.0")
      json.serialize(DoubleNode.valueOf(-3.14))
        .shouldBe("-3.14")
      json.serialize(DoubleNode.valueOf(0.toDouble()))
        .shouldBe("0.0")
      json.serialize(DoubleNode.valueOf(3.14))
        .shouldBe("3.14")
      json.serialize(DoubleNode.valueOf(90210.toDouble()))
        .shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      // Deserialization uses DecimalNode.
    }
}
