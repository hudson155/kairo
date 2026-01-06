package kairo.serialization

import com.fasterxml.jackson.databind.node.FloatNode
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class FloatNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(FloatNode.valueOf(-90210F))
        .shouldBe("-90210.0")
      json.serialize(FloatNode.valueOf(-3.14F))
        .shouldBe("-3.14")
      json.serialize(FloatNode.valueOf(0F))
        .shouldBe("0.0")
      json.serialize(FloatNode.valueOf(3.14F))
        .shouldBe("3.14")
      json.serialize(FloatNode.valueOf(90210F))
        .shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      // Deserialization uses DecimalNode.
    }
}
