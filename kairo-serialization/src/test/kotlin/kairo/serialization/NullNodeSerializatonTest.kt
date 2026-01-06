package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.node.NullNode
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class NullNodeSerializatonTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(NullNode.getInstance())
        .shouldBe("null")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<NullNode>("null")
        .shouldBe(NullNode.getInstance())
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<NullNode>("true")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.NullNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BooleanNode"
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<NullNode>("0")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.NullNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode"
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<NullNode>("\"0\"")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.NullNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.TextNode"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<NullNode>("""{}""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.NullNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ObjectNode"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<NullNode>("""[]""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.NullNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ArrayNode"
      )
    }
}
