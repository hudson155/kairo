package kairo.serialization

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test does NOT attempt to comprehensively include other well-known types.
 */
@OptIn(KairoJson.RawJsonMapper::class)
internal class JsonNodeSerializationTest {
  private val json: KairoJson = KairoJson {
    pretty = true
  }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(
        ObjectNode(
          json.delegate.nodeFactory,
          mapOf(
            "boolean" to BooleanNode.getTrue(),
            "ints" to ArrayNode(
              json.delegate.nodeFactory,
              listOf(IntNode(1), IntNode(2), IntNode(3)),
            ),
            "nested" to ObjectNode(
              json.delegate.nodeFactory,
              mapOf("string" to TextNode("Hello, World!"))
            ),
          )
        ),
      ).shouldBe(
        """
          {
            "boolean": true,
            "ints": [
              1,
              2,
              3
            ],
            "nested": {
              "string": "Hello, World!"
            }
          }
        """.trimIndent(),
      )
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<JsonNode>(
        """
          {
            "boolean": true,
            "ints": [
              1,
              2,
              3
            ],
            "nested": {
              "string": "Hello, World!"
            }
          }
        """.trimIndent(),
      ).should { jsonNode ->
        jsonNode.shouldHaveSize(3)
        jsonNode["boolean"].shouldBe(BooleanNode.getTrue())
        jsonNode["ints"].shouldBeInstanceOf<ArrayNode>().should { ints ->
          ints.shouldHaveSize(3)
          ints[0].intValue().shouldBe(1)
          ints[1].intValue().shouldBe(2)
          ints[2].intValue().shouldBe(3)
        }
        jsonNode["nested"].shouldBeInstanceOf<ObjectNode>().should { nested ->
          nested["string"].shouldBeInstanceOf<TextNode>().should { string ->
            string.textValue().shouldBe("Hello, World!")
          }
        }
      }
    }
}
