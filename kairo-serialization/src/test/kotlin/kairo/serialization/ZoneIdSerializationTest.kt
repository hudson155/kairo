package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ZoneIdSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(ZoneId.of("Etc/GMT+12")).shouldBe("\"Etc/GMT+12\"")
      jsonMapper.writeValueAsString(ZoneId.of("UTC")).shouldBe("\"UTC\"")
      jsonMapper.writeValueAsString(ZoneId.of("Pacific/Kiritimati")).shouldBe("\"Pacific/Kiritimati\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<ZoneId>("\"Etc/GMT+12\"").shouldBe(ZoneId.of("Etc/GMT+12"))
      jsonMapper.readValue<ZoneId>("\"UTC\"").shouldBe(ZoneId.of("UTC"))
      jsonMapper.readValue<ZoneId>("\"Pacific/Kiritimati\"").shouldBe(ZoneId.of("Pacific/Kiritimati"))
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("\"pacific/kiritimati\"")
      }
    }

  @Test
  fun `deserialize, unknown`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("\"pacific/edmonton\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("null")
      }

      jsonMapper.readValue<ZoneId?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneId>("""[]""")
      }
    }
}
