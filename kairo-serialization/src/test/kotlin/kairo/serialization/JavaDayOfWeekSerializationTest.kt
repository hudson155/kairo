package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import java.time.DayOfWeek
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaDayOfWeekSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DayOfWeek.MONDAY).shouldBe("\"MONDAY\"")
      json.serialize(DayOfWeek.TUESDAY).shouldBe("\"TUESDAY\"")
      json.serialize(DayOfWeek.SUNDAY).shouldBe("\"SUNDAY\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DayOfWeek>("\"MONDAY\"").shouldBe(DayOfWeek.MONDAY)
      json.deserialize<DayOfWeek>("\"TUESDAY\"").shouldBe(DayOfWeek.TUESDAY)
      json.deserialize<DayOfWeek>("\"SUNDAY\"").shouldBe(DayOfWeek.SUNDAY)
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DayOfWeek>("\"tuesday\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from String \"tuesday\"",
      )
    }

  @Test
  fun `deserialize, wrong format (short)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DayOfWeek>("\"tue\"")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from String \"tue\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DayOfWeek>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified java.time.DayOfWeek(non-null) but was null",
      )

      json.deserialize<DayOfWeek?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DayOfWeek>("true")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from Boolean value",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DayOfWeek>("2")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from number 2: not allowed to deserialize Enum value out of number",
      )
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DayOfWeek>("""{}""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from Object value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DayOfWeek>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `java.time.DayOfWeek`" +
          " from Array value",
      )
    }
}
