package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.YearMonth
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class YearMonthSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(YearMonth.of(-2023, Month.JANUARY)).shouldBe("\"-2023-01\"")
      json.serialize(YearMonth.of(2023, Month.NOVEMBER)).shouldBe("\"2023-11\"")
      json.serialize(YearMonth.of(3716, Month.DECEMBER)).shouldBe("\"3716-12\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<YearMonth>("\"-2023-01\"").shouldBe(YearMonth.of(-2023, Month.JANUARY))
      json.deserialize<YearMonth>("\"2023-11\"").shouldBe(YearMonth.of(2023, Month.NOVEMBER))
      json.deserialize<YearMonth>("\"3716-12\"").shouldBe(YearMonth.of(3716, Month.DECEMBER))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("\"2023-00\"")
      }

      shouldThrowAny {
        json.deserialize<YearMonth>("\"2023-13\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("null")
      }

      json.deserialize<YearMonth?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("202311")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<YearMonth>("""[]""")
      }
    }
}
