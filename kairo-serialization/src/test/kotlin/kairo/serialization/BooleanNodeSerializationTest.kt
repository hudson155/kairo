package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.node.BooleanNode
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BooleanNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BooleanNode.getTrue())
        .shouldBe("true")
      json.serialize(BooleanNode.getFalse())
        .shouldBe("false")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BooleanNode>("true")
        .shouldBe(BooleanNode.getTrue())
      json.deserialize<BooleanNode>("false")
        .shouldBe(BooleanNode.getFalse())
    }

  @Test
  fun `deserialize, wrong format (all caps)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BooleanNode>("TRUE")
      }.message.shouldStartWith(
        "Unrecognized token 'TRUE'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BooleanNode>("FALSE")
      }.message.shouldStartWith(
        "Unrecognized token 'FALSE'",
      )
    }

  @Test
  fun `deserialize, wrong format (leading capital)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BooleanNode>("True")
      }.message.shouldStartWith(
        "Unrecognized token 'True'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BooleanNode>("False")
      }.message.shouldStartWith(
        "Unrecognized token 'False'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.NullNode",
      )
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode?>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode?" +
          " but was com.fasterxml.jackson.databind.node.NullNode",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("-1")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("0")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("1")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("\"true\"")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.TextNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("\"false\"")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.TextNode",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("""{}""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ObjectNode",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BooleanNode>("""[]""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BooleanNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ArrayNode",
      )
    }
}
