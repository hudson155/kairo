package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.node.TextNode
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class TextNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(TextNode.valueOf(""))
        .shouldBe("\"\"")
      json.serialize(TextNode.valueOf("Hello, World!"))
        .shouldBe("\"Hello, World!\"")
      json.serialize(TextNode.valueOf("✝\uFE0F"))
        .shouldBe("\"✝\uFE0F\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<TextNode>("\"\"")
        .shouldBe(TextNode.valueOf(""))
      json.deserialize<TextNode>("\"Hello, World!\"")
        .shouldBe(TextNode.valueOf("Hello, World!"))
      json.deserialize<TextNode>("\"✝\uFE0F\"")
        .shouldBe(TextNode.valueOf("✝\uFE0F"))
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.NullNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode?>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode?" +
          " but was com.fasterxml.jackson.databind.node.NullNode"
      )
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode>("true")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BooleanNode"
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode>("0")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode>("""{}""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ObjectNode"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<TextNode>("""[]""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.TextNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ArrayNode"
      )
    }
}
