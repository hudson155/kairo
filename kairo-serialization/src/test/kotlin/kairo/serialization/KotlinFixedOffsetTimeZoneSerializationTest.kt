package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.FixedOffsetTimeZone
import kotlinx.datetime.UtcOffset
import org.junit.jupiter.api.Test

internal class KotlinFixedOffsetTimeZoneSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(FixedOffsetTimeZone(UtcOffset(-18))).shouldBe("\"-18:00\"")
      json.serialize(FixedOffsetTimeZone(UtcOffset.ZERO)).shouldBe("\"Z\"")
      json.serialize(FixedOffsetTimeZone(UtcOffset(18))).shouldBe("\"+18:00\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<FixedOffsetTimeZone>("\"-18:00\"").shouldBe(FixedOffsetTimeZone(UtcOffset(-18)))
      json.deserialize<FixedOffsetTimeZone>("\"Z\"").shouldBe(FixedOffsetTimeZone(UtcOffset.ZERO))
      json.deserialize<FixedOffsetTimeZone>("\"+18:00\"").shouldBe(FixedOffsetTimeZone(UtcOffset(18)))
    }

  @Test
  fun `deserialize, out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("\"-19:00\"")
      }

      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("\"+19:00\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("null")
      }

      json.deserialize<FixedOffsetTimeZone?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<FixedOffsetTimeZone>("""[]""")
      }
    }
}
