package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.node.BigIntegerNode
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigInteger
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BigIntegerNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(BigIntegerNode.valueOf(BigInteger("-1234567890987654321")))
        .shouldBe("-1234567890987654321")
      json.serialize(BigIntegerNode.valueOf(BigInteger("0")))
        .shouldBe("0")
      json.serialize(BigIntegerNode.valueOf(BigInteger("1234567890987654321")))
        .shouldBe("1234567890987654321")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<BigIntegerNode>("-1234567890987654321")
        .shouldBe(BigIntegerNode.valueOf(BigInteger("-1234567890987654321")))
      json.deserialize<BigIntegerNode>("-0")
        .shouldBe(BigIntegerNode.valueOf(BigInteger("0")))
      json.deserialize<BigIntegerNode>("0")
        .shouldBe(BigIntegerNode.valueOf(BigInteger("0")))
      json.deserialize<BigIntegerNode>("1234567890987654321")
        .shouldBe(BigIntegerNode.valueOf(BigInteger("1234567890987654321")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<BigIntegerNode>("1 234567890987654321")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `com.fasterxml.jackson.databind.node.BigIntegerNode`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigIntegerNode>("01234567890987654321")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (scientific notation)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("1e0")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.DecimalNode",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigIntegerNode>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigIntegerNode>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigIntegerNode>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<BigIntegerNode>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.NullNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode?>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode?" +
          " but was com.fasterxml.jackson.databind.node.NullNode"
      )
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("true")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BooleanNode",
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("0.0")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.DecimalNode",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("\"0\"")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.TextNode",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("""{}""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ObjectNode",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<BigIntegerNode>("""[]""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.BigIntegerNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ArrayNode",
      )
    }
}
