package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.Period
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaPeriodSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Period.of(-2, -3, -4)).shouldBe("\"P-2Y-3M-4D\"")
      json.serialize(Period.ofYears(-5)).shouldBe("\"P-5Y\"")
      json.serialize(Period.ofMonths(-5)).shouldBe("\"P-5M\"")
      json.serialize(Period.ofWeeks(-5)).shouldBe("\"P-35D\"")
      json.serialize(Period.ofDays(-5)).shouldBe("\"P-5D\"")
      json.serialize(Period.ZERO).shouldBe("\"P0D\"")
      json.serialize(Period.ofDays(5)).shouldBe("\"P5D\"")
      json.serialize(Period.ofWeeks(5)).shouldBe("\"P35D\"")
      json.serialize(Period.ofMonths(5)).shouldBe("\"P5M\"")
      json.serialize(Period.ofYears(5)).shouldBe("\"P5Y\"")
      json.serialize(Period.of(2, 3, 4)).shouldBe("\"P2Y3M4D\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Period>("\"P-2Y-3M-4D\"").shouldBe(Period.of(-2, -3, -4))
      json.deserialize<Period>("\"P-5Y\"").shouldBe(Period.ofYears(-5))
      json.deserialize<Period>("\"P-5M\"").shouldBe(Period.ofMonths(-5))
      json.deserialize<Period>("\"P-35D\"").shouldBe(Period.ofDays(-35))
      json.deserialize<Period>("\"P-5D\"").shouldBe(Period.ofDays(-5))
      json.deserialize<Period>("\"P0D\"").shouldBe(Period.ZERO)
      json.deserialize<Period>("\"P5D\"").shouldBe(Period.ofDays(5))
      json.deserialize<Period>("\"P35D\"").shouldBe(Period.ofDays(35))
      json.deserialize<Period>("\"P5M\"").shouldBe(Period.ofMonths(5))
      json.deserialize<Period>("\"P5Y\"").shouldBe(Period.ofYears(5))
      json.deserialize<Period>("\"P2Y3M4D\"").shouldBe(Period.of(2, 3, 4))
    }

  @Test
  fun `deserialize, wrong format (missing p)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Period>("\"0D\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from String \"0D\"",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Period>("\"P 0D\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from String \"P 0D\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Period>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.Period(non-null) but was null",
      )

      json.deserialize<Period?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Period>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Period>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Period>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Period>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from Array value",
      )
    }
}
