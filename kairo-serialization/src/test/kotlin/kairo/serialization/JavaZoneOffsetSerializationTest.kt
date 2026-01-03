package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class JavaZoneOffsetSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(ZoneOffset.MIN).shouldBe("\"-18:00\"")
      json.serialize(ZoneOffset.UTC).shouldBe("\"Z\"")
      json.serialize(ZoneOffset.MAX).shouldBe("\"+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<ZoneOffset>("\"-18:00\"").shouldBe(ZoneOffset.MIN)
      json.deserialize<ZoneOffset>("\"Z\"").shouldBe(ZoneOffset.UTC)
      json.deserialize<ZoneOffset>("\"+18:00\"").shouldBe(ZoneOffset.MAX)
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("\"-19:00\"")
      }

      shouldThrowAny {
        json.deserialize<ZoneOffset>("\"+19:00\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("null")
      }

      json.deserialize<ZoneOffset?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<ZoneOffset>("""[]""")
      }
    }
}
