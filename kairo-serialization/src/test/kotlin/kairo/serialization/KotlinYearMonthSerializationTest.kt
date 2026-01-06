package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.junit.jupiter.api.Test

internal class KotlinYearMonthSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(YearMonth(-2023, Month.JANUARY))
        .shouldBe("\"-2023-01\"")
      json.serialize(YearMonth(2023, Month.NOVEMBER))
        .shouldBe("\"2023-11\"")
      json.serialize(YearMonth(3716, Month.DECEMBER))
        .shouldBe("\"3716-12\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<YearMonth>("\"-2023-01\"")
        .shouldBe(YearMonth(-2023, Month.JANUARY))
      json.deserialize<YearMonth>("\"2023-11\"")
        .shouldBe(YearMonth(2023, Month.NOVEMBER))
      json.deserialize<YearMonth>("\"3716-12\"")
        .shouldBe(YearMonth(3716, Month.DECEMBER))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<YearMonth>("\"2023-00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.YearMonth`" +
          " from String \"2023-00\"",
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<YearMonth>("\"2023-13\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.YearMonth`" +
          " from String \"2023-13\"",
      )
    }

  @Test
  fun `deserialize, wrong format (missing dash)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<YearMonth>("\"202311\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.YearMonth`" +
          " from String \"202311\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<YearMonth>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlinx.datetime.YearMonth(non-null) but was null",
      )

      json.deserialize<YearMonth?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<YearMonth>("true")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_TRUE)",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<YearMonth>("202311")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<YearMonth>("""{}""")
      }.message.shouldStartWith(
        "Unexpected token (START_OBJECT)",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<NullPointerException> {
        json.deserialize<YearMonth>("""[]""")
      }.message.shouldBeNull() // This seems like a bug in Jackson! I don't think this should be null.
    }
}
