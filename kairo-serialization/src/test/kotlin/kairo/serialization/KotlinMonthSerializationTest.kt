package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Month
import org.junit.jupiter.api.Test

internal class KotlinMonthSerializationTest {
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
      }
    }

  @Test
  fun `deserialize, wrong format (short)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Month>("\"NOV\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Month>("null")
      }

      json.deserialize<Month?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Month>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Month>("11")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Month>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Month>("""[]""")
      }
    }
}
