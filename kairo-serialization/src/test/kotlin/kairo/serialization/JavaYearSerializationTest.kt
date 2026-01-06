package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.Year
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaYearSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Year.of(-2023)).shouldBe("\"-2023\"")
      json.serialize(Year.of(2023)).shouldBe("\"2023\"")
      json.serialize(Year.of(3716)).shouldBe("\"3716\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      json.deserialize<Year>("\"-2023\"").shouldBe(Year.of(-2023))
      json.deserialize<Year>("\"2023\"").shouldBe(Year.of(2023))
      json.deserialize<Year>("\"3716\"").shouldBe(Year.of(3716))
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      json.deserialize<Year>("-2023").shouldBe(Year.of(-2023))
      json.deserialize<Year>("2023").shouldBe(Year.of(2023))
      json.deserialize<Year>("3716").shouldBe(Year.of(3716))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Year>("\"2 023\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Year`" +
          " from String \"2 023\""
      )
    }

  @Test
  fun `deserialize, wrong format (has comma)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Year>("\"2,023\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Year`" +
          " from String \"2,023\""
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Year>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.Year(non-null) but was null"
      )

      json.deserialize<Year?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Year>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)"
      )
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Year>("2023.0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_FLOAT)"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Year>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Year>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Year`" +
          " from Array value"
      )
    }
}
