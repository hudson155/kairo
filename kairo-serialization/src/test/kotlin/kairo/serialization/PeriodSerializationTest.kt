package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Period
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PeriodSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(Period.of(-2, -3, -4)).shouldBe("\"P-2Y-3M-4D\"")
      jsonMapper.writeValueAsString(Period.ofYears(-5)).shouldBe("\"P-5Y\"")
      jsonMapper.writeValueAsString(Period.ofMonths(-5)).shouldBe("\"P-5M\"")
      jsonMapper.writeValueAsString(Period.ofWeeks(-5)).shouldBe("\"P-35D\"")
      jsonMapper.writeValueAsString(Period.ofDays(-5)).shouldBe("\"P-5D\"")
      jsonMapper.writeValueAsString(Period.ZERO).shouldBe("\"P0D\"")
      jsonMapper.writeValueAsString(Period.ofDays(5)).shouldBe("\"P5D\"")
      jsonMapper.writeValueAsString(Period.ofWeeks(5)).shouldBe("\"P35D\"")
      jsonMapper.writeValueAsString(Period.ofMonths(5)).shouldBe("\"P5M\"")
      jsonMapper.writeValueAsString(Period.ofYears(5)).shouldBe("\"P5Y\"")
      jsonMapper.writeValueAsString(Period.of(2, 3, 4)).shouldBe("\"P2Y3M4D\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Period>("\"P-2Y-3M-4D\"").shouldBe(Period.of(-2, -3, -4))
      jsonMapper.readValue<Period>("\"P-5Y\"").shouldBe(Period.ofYears(-5))
      jsonMapper.readValue<Period>("\"P-5M\"").shouldBe(Period.ofMonths(-5))
      jsonMapper.readValue<Period>("\"P-35D\"").shouldBe(Period.ofDays(-35))
      jsonMapper.readValue<Period>("\"P-5D\"").shouldBe(Period.ofDays(-5))
      jsonMapper.readValue<Period>("\"P0D\"").shouldBe(Period.ZERO)
      jsonMapper.readValue<Period>("\"P5D\"").shouldBe(Period.ofDays(5))
      jsonMapper.readValue<Period>("\"P35D\"").shouldBe(Period.ofDays(35))
      jsonMapper.readValue<Period>("\"P5M\"").shouldBe(Period.ofMonths(5))
      jsonMapper.readValue<Period>("\"P5Y\"").shouldBe(Period.ofYears(5))
      jsonMapper.readValue<Period>("\"P2Y3M4D\"").shouldBe(Period.of(2, 3, 4))
    }

  @Test
  fun `deserialize, wrong format (missing p)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("\"0D\"")
      }
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("\"P 0D\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("null")
      }

      jsonMapper.readValue<Period?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Period>("""[]""")
      }
    }
}
