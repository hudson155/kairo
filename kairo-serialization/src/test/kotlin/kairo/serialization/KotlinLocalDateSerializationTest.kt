package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.junit.jupiter.api.Test

internal class KotlinLocalDateSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalDate(-2023, Month.JANUARY, 1))
        .shouldBe("\"-2023-01-01\"")
      json.serialize(LocalDate(2023, Month.NOVEMBER, 14))
        .shouldBe("\"2023-11-14\"")
      json.serialize(LocalDate(3716, Month.DECEMBER, 30))
        .shouldBe("\"3716-12-30\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<LocalDate>("\"-2023-01-01\"")
        .shouldBe(LocalDate(-2023, Month.JANUARY, 1))
      json.deserialize<LocalDate>("\"2023-11-14\"")
        .shouldBe(LocalDate(2023, Month.NOVEMBER, 14))
      json.deserialize<LocalDate>("\"3716-12-30\"")
        .shouldBe(LocalDate(3716, Month.DECEMBER, 30))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("\"2023-00-14\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalDate`" +
          " from String \"2023-00-14\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("\"2023-13-14\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalDate`" +
          " from String \"2023-13-14\""
      )
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("\"2023-11-00\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalDate`" +
          " from String \"2023-11-00\""
      )

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("\"2023-11-31\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalDate`" +
          " from String \"2023-11-31\""
      )
    }

  @Test
  fun `deserialize, wrong format (missing dashes)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("\"20231114\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.LocalDate`" +
          " from String \"20231114\""
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<LocalDate>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kotlinx.datetime.LocalDate(non-null) but was null"
      )

      json.deserialize<LocalDate?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDate>("true")
      }.message.shouldStartWith(
        "Expected array or string"
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDate>("20231114")
      }.message.shouldStartWith(
        "Cannot coerce Integer value (20231114)" +
          " to `java.time.LocalDate` value"
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDate>("""{}""")
      }.message.shouldStartWith(
        "Expected array or string"
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<NullPointerException> {
        json.deserialize<LocalDate>("""[]""")
      }.message.shouldBeNull() // This seems like a bug in Jackson! I don't think this should be null.
    }
}
