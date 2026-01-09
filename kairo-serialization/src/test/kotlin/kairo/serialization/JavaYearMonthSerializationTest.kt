package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.Month
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaYearMonthSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(YearMonth.of(-2023, Month.JANUARY))
        .shouldBe("\"-2023-01\"")
      json.serialize(YearMonth.of(2023, Month.NOVEMBER))
        .shouldBe("\"2023-11\"")
      json.serialize(YearMonth.of(3716, Month.DECEMBER))
        .shouldBe("\"3716-12\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<YearMonth>("\"-2023-01\"")
        .shouldBe(YearMonth.of(-2023, Month.JANUARY))
      json.deserialize<YearMonth>("\"2023-11\"")
        .shouldBe(YearMonth.of(2023, Month.NOVEMBER))
      json.deserialize<YearMonth>("\"3716-12\"")
        .shouldBe(YearMonth.of(3716, Month.DECEMBER))
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
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<YearMonth>("202311")
      }.message.shouldStartWith(
        "Unexpected token (VALUE_NUMBER_INT)",
      )
    }
}
