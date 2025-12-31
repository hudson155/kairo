package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Year
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class YearSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(Year.of(-2023)).shouldBe("\"-2023\"")
      jsonMapper.writeValueAsString(Year.of(2023)).shouldBe("\"2023\"")
      jsonMapper.writeValueAsString(Year.of(3716)).shouldBe("\"3716\"")
    }

  @Test
  fun `deserialize, string`(): Unit =
    runTest {
      jsonMapper.readValue<Year>("\"-2023\"").shouldBe(Year.of(-2023))
      jsonMapper.readValue<Year>("\"2023\"").shouldBe(Year.of(2023))
      jsonMapper.readValue<Year>("\"3716\"").shouldBe(Year.of(3716))
    }

  @Test
  fun `deserialize, int`(): Unit =
    runTest {
      jsonMapper.readValue<Year>("-2023").shouldBe(Year.of(-2023))
      jsonMapper.readValue<Year>("2023").shouldBe(Year.of(2023))
      jsonMapper.readValue<Year>("3716").shouldBe(Year.of(3716))
    }

  @Test
  fun `deserialize, wrong format (has space)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("\"2 023\"")
      }
    }

  @Test
  fun `deserialize, wrong format (has comma)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("\"2,023\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("null")
      }

      jsonMapper.readValue<Year?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("2023.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Year>("""[]""")
      }
    }
}
