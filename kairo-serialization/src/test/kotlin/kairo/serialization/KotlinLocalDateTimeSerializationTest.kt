package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class KotlinLocalDateTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalDateTime(-2023, Month.JANUARY, 1, 0, 0, 0, 0))
        .shouldBe("\"-2023-01-01T00:00:00\"")
      json.serialize(LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789))
        .shouldBe("\"2023-11-14T22:13:20.123456789\"")
      json.serialize(LocalDateTime(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999))
        .shouldBe("\"3716-12-30T23:59:59.999999999\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<LocalDateTime>("\"-2023-01-01T00:00:00\"")
        .shouldBe(LocalDateTime(-2023, Month.JANUARY, 1, 0, 0, 0, 0))
      json.deserialize<LocalDateTime>("\"2023-11-14T22:13:20.123456789\"")
        .shouldBe(LocalDateTime(2023, Month.NOVEMBER, 14, 22, 13, 20, 123456789))
      json.deserialize<LocalDateTime>("\"3716-12-30T23:59:59.999999999\"")
        .shouldBe(LocalDateTime(3716, Month.DECEMBER, 30, 23, 59, 59, 999999999))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-00-14T22:13:20.123456789\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-13-14T22:13:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-00T22:13:20.123456789\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-31T22:13:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T-01:13:20.123456789\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T24:13:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T22:-01:20.123456789\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T22:60:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T22:13:-01.123456789\"")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<LocalDateTime>("\"2023-11-14T22:13:60.123456789\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<LocalDateTime>("null")
      }

      json.deserialize<LocalDateTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDateTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDateTime>("20231114221320123456789")
      }

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDateTime>("1700000000")
      }

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDateTime>("1700000000123456789")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<LocalDateTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<NullPointerException> {
        json.deserialize<LocalDateTime>("""[]""")
      }
    }
}
