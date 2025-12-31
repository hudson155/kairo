package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class YearMonthSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(YearMonth.of(-2023, Month.JANUARY)).shouldBe("\"-2023-01\"")
      jsonMapper.writeValueAsString(YearMonth.of(2023, Month.NOVEMBER)).shouldBe("\"2023-11\"")
      jsonMapper.writeValueAsString(YearMonth.of(3716, Month.DECEMBER)).shouldBe("\"3716-12\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<YearMonth>("\"-2023-01\"").shouldBe(YearMonth.of(-2023, Month.JANUARY))
      jsonMapper.readValue<YearMonth>("\"2023-11\"").shouldBe(YearMonth.of(2023, Month.NOVEMBER))
      jsonMapper.readValue<YearMonth>("\"3716-12\"").shouldBe(YearMonth.of(3716, Month.DECEMBER))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("\"2023-00\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("\"2023-13\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("null")
      }

      jsonMapper.readValue<YearMonth?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("202311")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("202311.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<YearMonth>("""[]""")
      }
    }
}
