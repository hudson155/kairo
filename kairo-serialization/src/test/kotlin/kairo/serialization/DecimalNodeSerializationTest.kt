package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.node.DecimalNode
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.math.BigDecimal
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DecimalNodeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DecimalNode.valueOf(BigDecimal("-90210.0")))
        .shouldBe("-90210.0")
      json.serialize(DecimalNode.valueOf(BigDecimal("-90210")))
        .shouldBe("-90210")
      json.serialize(DecimalNode.valueOf(BigDecimal("-3.14")))
        .shouldBe("-3.14")
      json.serialize(DecimalNode.valueOf(BigDecimal("-0.0")))
        .shouldBe("0.0")
      json.serialize(DecimalNode.valueOf(BigDecimal("0")))
        .shouldBe("0")
      json.serialize(DecimalNode.valueOf(BigDecimal("0.0")))
        .shouldBe("0.0")
      json.serialize(DecimalNode.valueOf(BigDecimal("3.14")))
        .shouldBe("3.14")
      json.serialize(DecimalNode.valueOf(BigDecimal("90210")))
        .shouldBe("90210")
      json.serialize(DecimalNode.valueOf(BigDecimal("90210.0")))
        .shouldBe("90210.0")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DecimalNode>("-90210.0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("-90210.0")))
      json.deserialize<DecimalNode>("-3.14")
        .shouldBe(DecimalNode.valueOf(BigDecimal("-3.14")))
      json.deserialize<DecimalNode>("-1e0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("-1")))
      json.deserialize<DecimalNode>("-0.0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("-0.0")))
      json.deserialize<DecimalNode>("0.0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("0.0")))
      json.deserialize<DecimalNode>("1e0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("1")))
      json.deserialize<DecimalNode>("3.14")
        .shouldBe(DecimalNode.valueOf(BigDecimal("3.14")))
      json.deserialize<DecimalNode>("90210.0")
        .shouldBe(DecimalNode.valueOf(BigDecimal("90210.0")))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("9 0210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DecimalNode>("3.1 4")
      }.message.shouldStartWith(
        "Trailing token (of type VALUE_NUMBER_INT) found after value" +
          " (bound as `com.fasterxml.jackson.databind.node.DecimalNode`)",
      )
    }

  @Test
  fun `deserialize, wrong format (leading 0)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("090210")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("03.14")
      }.message.shouldStartWith(
        "Invalid numeric value: Leading zeroes not allowed",
      )
    }

  @Test
  fun `deserialize, wrong format (hex)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("0x0")
      }.message.shouldStartWith(
        "Unexpected character ('x' (code 120))" +
          ": Expected space separating root-level values",
      )
    }

  @Test
  fun `deserialize, wrong format (NaN)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("NaN")
      }.message.shouldStartWith(
        "Non-standard token 'NaN'",
      )
    }

  @Test
  fun `deserialize, wrong format (Infinity)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("Infinity")
      }.message.shouldStartWith(
        "Non-standard token 'Infinity'",
      )

      shouldThrowExactly<JsonParseException> {
        json.deserialize<DecimalNode>("-Infinity")
      }.message.shouldStartWith(
        "Non-standard token '-Infinity'",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.NullNode",
      )

      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode?>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode?" +
          " but was com.fasterxml.jackson.databind.node.NullNode"
      )
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("true")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BooleanNode",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("\"0\"")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.TextNode",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("90210")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.BigIntegerNode",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("""{}""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ObjectNode",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DecimalNode>("""[]""")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified com.fasterxml.jackson.databind.node.DecimalNode(non-null)" +
          " but was com.fasterxml.jackson.databind.node.ArrayNode",
      )
    }
}
