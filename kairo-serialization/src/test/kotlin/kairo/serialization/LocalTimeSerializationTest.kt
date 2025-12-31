package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class LocalTimeSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(LocalTime.of(0, 0, 0, 0)).shouldBe("\"00:00:00\"")
      jsonMapper.writeValueAsString(LocalTime.of(22, 13, 20, 123456789)).shouldBe("\"22:13:20.123456789\"")
      jsonMapper.writeValueAsString(LocalTime.of(23, 59, 59, 999999999)).shouldBe("\"23:59:59.999999999\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<LocalTime>("\"00:00:00\"").shouldBe(LocalTime.of(0, 0, 0, 0))
      jsonMapper.readValue<LocalTime>("\"22:13:20.123456789\"").shouldBe(LocalTime.of(22, 13, 20, 123456789))
      jsonMapper.readValue<LocalTime>("\"23:59:59.999999999\"").shouldBe(LocalTime.of(23, 59, 59, 999999999))
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"-01:13:20.123456789\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"24:13:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"22:-01:20.123456789\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"22:60:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"22:13:-01.123456789\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("\"22:13:60.123456789\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("null")
      }

      jsonMapper.readValue<LocalTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("221360123456789")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("221360123456789.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<LocalTime>("""[]""")
      }
    }
}
