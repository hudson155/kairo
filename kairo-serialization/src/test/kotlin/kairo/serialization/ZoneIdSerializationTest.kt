package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ZoneIdSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ZoneId.of("Etc/GMT+12")).shouldBe("\"Etc/GMT+12\"")
      json.serialize(ZoneId.of("UTC")).shouldBe("\"UTC\"")
      json.serialize(ZoneId.of("Pacific/Kiritimati")).shouldBe("\"Pacific/Kiritimati\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ZoneId>("\"Etc/GMT+12\"").shouldBe(ZoneId.of("Etc/GMT+12"))
      json.deserialize<ZoneId>("\"UTC\"").shouldBe(ZoneId.of("UTC"))
      json.deserialize<ZoneId>("\"Pacific/Kiritimati\"").shouldBe(ZoneId.of("Pacific/Kiritimati"))
    }

  @Test
  fun `deserialize, wrong format (lowercase)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("\"pacific/kiritimati\"")
      }
    }

  @Test
  fun `deserialize, unknown`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("\"Pacific/Edmonton\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("null")
      }

      json.deserialize<ZoneId?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneId>("""[]""")
      }
    }
}
