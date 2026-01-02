package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LocalDateSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalDate.of(-2023, Month.JANUARY, 1)).shouldBe("\"-2023-01-01\"")
      json.serialize(LocalDate.of(2023, Month.NOVEMBER, 14)).shouldBe("\"2023-11-14\"")
      json.serialize(LocalDate.of(3716, Month.DECEMBER, 30)).shouldBe("\"3716-12-30\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<LocalDate>("\"-2023-01-01\"").shouldBe(LocalDate.of(-2023, Month.JANUARY, 1))
      json.deserialize<LocalDate>("\"2023-11-14\"").shouldBe(LocalDate.of(2023, Month.NOVEMBER, 14))
      json.deserialize<LocalDate>("\"3716-12-30\"").shouldBe(LocalDate.of(3716, Month.DECEMBER, 30))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("\"2023-00-14\"")
      }

      shouldThrowAny {
        json.deserialize<LocalDate>("\"2023-13-14\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("\"2023-11-00\"")
      }

      shouldThrowAny {
        json.deserialize<LocalDate>("\"2023-11-31\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("null")
      }

      json.deserialize<LocalDate?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("20231114")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalDate>("""[]""")
      }
    }
}
