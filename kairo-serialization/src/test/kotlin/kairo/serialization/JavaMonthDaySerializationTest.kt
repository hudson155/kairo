package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.MonthDay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaMonthDaySerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(MonthDay.of(Month.JANUARY, 1))
        .shouldBe("\"--01-01\"")
      json.serialize(MonthDay.of(Month.NOVEMBER, 14))
        .shouldBe("\"--11-14\"")
      json.serialize(MonthDay.of(Month.DECEMBER, 30))
        .shouldBe("\"--12-30\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<MonthDay>("\"--01-01\"")
        .shouldBe(MonthDay.of(Month.JANUARY, 1))
      json.deserialize<MonthDay>("\"--11-14\"")
        .shouldBe(MonthDay.of(Month.NOVEMBER, 14))
      json.deserialize<MonthDay>("\"--12-30\"")
        .shouldBe(MonthDay.of(Month.DECEMBER, 30))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<MonthDay>("\"--00-14\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<MonthDay>("\"--13-14\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<MonthDay>("\"--11-00\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<MonthDay>("\"--11-31\"")
      }
    }

  @Test
  fun `deserialize, wrong format (missing dashes)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<MonthDay>("\"1114\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<MonthDay>("null")
      }

      json.deserialize<MonthDay?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<MonthDay>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<MonthDay>("1114")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<MonthDay>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<MonthDay>("""[]""")
      }
    }
}
