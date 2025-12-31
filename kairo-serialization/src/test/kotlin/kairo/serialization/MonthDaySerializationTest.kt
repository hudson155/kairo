package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Month
import java.time.MonthDay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MonthDaySerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(MonthDay.of(Month.JANUARY, 1)).shouldBe("\"--01-01\"")
      jsonMapper.writeValueAsString(MonthDay.of(Month.NOVEMBER, 14)).shouldBe("\"--11-14\"")
      jsonMapper.writeValueAsString(MonthDay.of(Month.DECEMBER, 30)).shouldBe("\"--12-30\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<MonthDay>("\"--01-01\"").shouldBe(MonthDay.of(Month.JANUARY, 1))
      jsonMapper.readValue<MonthDay>("\"--11-14\"").shouldBe(MonthDay.of(Month.NOVEMBER, 14))
      jsonMapper.readValue<MonthDay>("\"--12-30\"").shouldBe(MonthDay.of(Month.DECEMBER, 30))
    }

  @Test
  fun `deserialize, month out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("\"--00-14\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("\"--13-14\"")
      }
    }

  @Test
  fun `deserialize, day out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("\"--11-00\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("\"--11-31\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("null")
      }

      jsonMapper.readValue<MonthDay?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("1114")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("1114.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<MonthDay>("""[]""")
      }
    }
}
