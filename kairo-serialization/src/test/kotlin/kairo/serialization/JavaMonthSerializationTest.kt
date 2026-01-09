package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaMonthSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Month.JANUARY).shouldBe("\"JANUARY\"")
      json.serialize(Month.NOVEMBER).shouldBe("\"NOVEMBER\"")
      json.serialize(Month.DECEMBER).shouldBe("\"DECEMBER\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Month>("\"JANUARY\"").shouldBe(Month.JANUARY)
      json.deserialize<Month>("\"NOVEMBER\"").shouldBe(Month.NOVEMBER)
      json.deserialize<Month>("\"DECEMBER\"").shouldBe(Month.DECEMBER)
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Month>("\"november\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Month`" +
          " from String \"november\"",
      )
    }

  @Test
  fun `deserialize, wrong format (short)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Month>("\"NOV\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Month`" +
          " from String \"NOV\"",
      )
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Month>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Month`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Month>("11")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.Month`" +
          " from number 11: not allowed to deserialize Enum value out of number",
      )
    }
}
