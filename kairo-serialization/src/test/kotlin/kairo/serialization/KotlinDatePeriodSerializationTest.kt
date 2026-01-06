package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import org.junit.jupiter.api.Test

internal class KotlinDatePeriodSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DatePeriod(years = -2, months = -3, days = -4)).shouldBe("\"P-2Y-3M-4D\"")
      json.serialize(DatePeriod(years = -5)).shouldBe("\"P-5Y\"")
      json.serialize(DatePeriod(months = -5)).shouldBe("\"P-5M\"")
      json.serialize(DatePeriod(days = -5)).shouldBe("\"P-5D\"")
      json.serialize(DatePeriod()).shouldBe("\"P0D\"")
      json.serialize(DatePeriod(days = 5)).shouldBe("\"P5D\"")
      json.serialize(DatePeriod(months = 5)).shouldBe("\"P5M\"")
      json.serialize(DatePeriod(years = 5)).shouldBe("\"P5Y\"")
      json.serialize(DatePeriod(years = 2, months = 3, days = 4)).shouldBe("\"P2Y3M4D\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DatePeriod>("\"P-2Y-3M-4D\"").shouldBe(DatePeriod(years = -2, months = -3, days = -4))
      json.deserialize<DatePeriod>("\"P-5Y\"").shouldBe(DatePeriod(years = -5))
      json.deserialize<DatePeriod>("\"P-5M\"").shouldBe(DatePeriod(months = -5))
      json.deserialize<DatePeriod>("\"P-5D\"").shouldBe(DatePeriod(days = -5))
      json.deserialize<DatePeriod>("\"P0D\"").shouldBe(DatePeriod())
      json.deserialize<DatePeriod>("\"P5D\"").shouldBe(DatePeriod(days = 5))
      json.deserialize<DatePeriod>("\"P5M\"").shouldBe(DatePeriod(months = 5))
      json.deserialize<DatePeriod>("\"P5Y\"").shouldBe(DatePeriod(years = 5))
      json.deserialize<DatePeriod>("\"P2Y3M4D\"").shouldBe(DatePeriod(years = 2, months = 3, days = 4))
    }

  @Test
  fun `deserialize, wrong format (missing p)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DatePeriod>("\"0D\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from String \"0D\"",
      )
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DatePeriod>("\"P 0D\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from String \"P 0D\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DatePeriod>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlinx.datetime.DatePeriod(non-null) but was null",
      )

      json.deserialize<DatePeriod?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DatePeriod>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DatePeriod>("0")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DatePeriod>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DatePeriod>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Period`" +
          " from Array value",
      )
    }
}
