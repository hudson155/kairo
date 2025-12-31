package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Month
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MonthSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(Month.JANUARY).shouldBe("\"JANUARY\"")
      jsonMapper.writeValueAsString(Month.NOVEMBER).shouldBe("\"NOVEMBER\"")
      jsonMapper.writeValueAsString(Month.DECEMBER).shouldBe("\"DECEMBER\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Month>("\"JANUARY\"").shouldBe(Month.JANUARY)
      jsonMapper.readValue<Month>("\"NOVEMBER\"").shouldBe(Month.NOVEMBER)
      jsonMapper.readValue<Month>("\"DECEMBER\"").shouldBe(Month.DECEMBER)
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("\"november\"")
      }
    }

  @Test
  fun `deserialize, wrong format (short)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("\"NOV\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("null")
      }

      jsonMapper.readValue<Month?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("11")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("11.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Month>("""[]""")
      }
    }
}
