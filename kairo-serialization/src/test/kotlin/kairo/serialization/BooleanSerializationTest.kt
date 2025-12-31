package kairo.serialization

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BooleanSerializationTest {
  private val jsonMapper: JsonMapper = kairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      jsonMapper.writeValueAsString(true).shouldBe("true")
      jsonMapper.writeValueAsString(false).shouldBe("false")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      jsonMapper.readValue<Boolean>("true").shouldBeTrue()
      jsonMapper.readValue<Boolean>("false").shouldBeFalse()
    }

  @Test
  fun `deserialize, wrong format (all caps)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("TRUE")
      }

      shouldThrowAny {
        jsonMapper.readValue<Boolean>("FALSE")
      }
    }

  @Test
  fun `deserialize, wrong format (leading capital)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("True")
      }

      shouldThrowAny {
        jsonMapper.readValue<Boolean>("False")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("null")
      }

      jsonMapper.readValue<Boolean?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("-1")
      }

      shouldThrowAny {
        jsonMapper.readValue<Boolean>("0")
      }

      shouldThrowAny {
        jsonMapper.readValue<Boolean>("1")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("\"true\"")
      }

      shouldThrowAny {
        jsonMapper.readValue<Boolean>("\"false\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        jsonMapper.readValue<Boolean>("""[]""")
      }
    }
}
