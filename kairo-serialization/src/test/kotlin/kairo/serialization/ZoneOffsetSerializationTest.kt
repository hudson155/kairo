package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ZoneOffsetSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(ZoneOffset.MIN).shouldBe("\"-18:00\"")
      jsonMapper.writeValueAsString(ZoneOffset.UTC).shouldBe("\"Z\"")
      jsonMapper.writeValueAsString(ZoneOffset.MAX).shouldBe("\"+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<ZoneOffset>("\"-18:00\"").shouldBe(ZoneOffset.MIN)
      jsonMapper.readValue<ZoneOffset>("\"Z\"").shouldBe(ZoneOffset.UTC)
      jsonMapper.readValue<ZoneOffset>("\"+18:00\"").shouldBe(ZoneOffset.MAX)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("\"-19:00\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("\"+19:00\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("null")
      }

      jsonMapper.readValue<ZoneOffset?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (float)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("0.0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<ZoneOffset>("""[]""")
      }
    }
}
