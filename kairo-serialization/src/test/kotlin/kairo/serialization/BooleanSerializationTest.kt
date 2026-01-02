package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BooleanSerializationTest {
  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(true).shouldBe("true")
      json.serialize(false).shouldBe("false")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Boolean>("true").shouldBeTrue()
      json.deserialize<Boolean>("false").shouldBeFalse()
    }

  @Test
  fun `deserialize, wrong format (all caps)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("TRUE")
      }

      shouldThrowAny {
        json.deserialize<Boolean>("FALSE")
      }
    }

  @Test
  fun `deserialize, wrong format (leading capital)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("True")
      }

      shouldThrowAny {
        json.deserialize<Boolean>("False")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("null")
      }

      json.deserialize<Boolean?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("-1")
      }

      shouldThrowAny {
        json.deserialize<Boolean>("0")
      }

      shouldThrowAny {
        json.deserialize<Boolean>("1")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("\"true\"")
      }

      shouldThrowAny {
        json.deserialize<Boolean>("\"false\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Boolean>("""[]""")
      }
    }
}
