package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.DayOfWeek
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DayOfWeekSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(DayOfWeek.MONDAY).shouldBe("\"MONDAY\"")
      jsonMapper.writeValueAsString(DayOfWeek.TUESDAY).shouldBe("\"TUESDAY\"")
      jsonMapper.writeValueAsString(DayOfWeek.SUNDAY).shouldBe("\"SUNDAY\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<DayOfWeek>("\"MONDAY\"").shouldBe(DayOfWeek.MONDAY)
      jsonMapper.readValue<DayOfWeek>("\"TUESDAY\"").shouldBe(DayOfWeek.TUESDAY)
      jsonMapper.readValue<DayOfWeek>("\"SUNDAY\"").shouldBe(DayOfWeek.SUNDAY)
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("\"tuesday\"")
      }
    }

  @Test
  fun `deserialize, wrong format (short)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("\"tue\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("null")
      }

      jsonMapper.readValue<DayOfWeek?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("2")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("2.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<DayOfWeek>("""[]""")
      }
    }
}
