package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LocalDateSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(LocalDate.of(-2023, Month.JANUARY, 1)).shouldBe("\"-2023-01-01\"")
      jsonMapper.writeValueAsString(LocalDate.of(2023, Month.NOVEMBER, 14)).shouldBe("\"2023-11-14\"")
      jsonMapper.writeValueAsString(LocalDate.of(3716, Month.DECEMBER, 30)).shouldBe("\"3716-12-30\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<LocalDate>("\"-2023-01-01\"").shouldBe(LocalDate.of(-2023, Month.JANUARY, 1))
      jsonMapper.readValue<LocalDate>("\"2023-11-14\"").shouldBe(LocalDate.of(2023, Month.NOVEMBER, 14))
      jsonMapper.readValue<LocalDate>("\"3716-12-30\"").shouldBe(LocalDate.of(3716, Month.DECEMBER, 30))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("\"2023-00-14\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("\"2023-13-14\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("\"2023-11-00\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("\"2023-11-31\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("null")
      }

      jsonMapper.readValue<LocalDate?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("20231114")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("20231114.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalDate>("""[]""")
      }
    }
}
