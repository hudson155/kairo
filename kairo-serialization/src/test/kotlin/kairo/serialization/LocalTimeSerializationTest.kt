package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalTime
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@Suppress("UnderscoresInNumericLiterals")
internal class LocalTimeSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(LocalTime.of(0, 0, 0, 0)).shouldBe("\"00:00:00\"")
      json.serialize(LocalTime.of(22, 13, 20, 123456789)).shouldBe("\"22:13:20.123456789\"")
      json.serialize(LocalTime.of(23, 59, 59, 999999999)).shouldBe("\"23:59:59.999999999\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<LocalTime>("\"00:00:00\"").shouldBe(LocalTime.of(0, 0, 0, 0))
      json.deserialize<LocalTime>("\"22:13:20.123456789\"").shouldBe(LocalTime.of(22, 13, 20, 123456789))
      json.deserialize<LocalTime>("\"23:59:59.999999999\"").shouldBe(LocalTime.of(23, 59, 59, 999999999))
    }

  @Test
  fun `deserialize, hour out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("\"-01:13:20.123456789\"")
      }

      shouldThrowAny {
        json.deserialize<LocalTime>("\"24:13:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, minute out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("\"22:-01:20.123456789\"")
      }

      shouldThrowAny {
        json.deserialize<LocalTime>("\"22:60:20.123456789\"")
      }
    }

  @Test
  fun `deserialize, second out of range`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("\"22:13:-01.123456789\"")
      }

      shouldThrowAny {
        json.deserialize<LocalTime>("\"22:13:60.123456789\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("null")
      }

      json.deserialize<LocalTime?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("221320")
      }

      shouldThrowAny {
        json.deserialize<LocalTime>("221320123456789")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<LocalTime>("""[]""")
      }
    }
}
