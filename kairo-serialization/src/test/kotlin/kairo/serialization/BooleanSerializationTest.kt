package kairo.serialization

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.kotest.assertions.throwables.shouldThrowExactly
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
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("TRUE")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("FALSE")
      }
    }

  @Test
  fun `deserialize, wrong format (leading capital)`(): Unit =
    runTest {
      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("True")
      }

      shouldThrowExactly<JsonParseException> {
        json.deserialize<Boolean>("False")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Boolean>("null")
      }

      json.deserialize<Boolean?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("-1")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("0")
      }

      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Boolean>("1")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("\"true\"")
      }

      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("\"false\"")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Boolean>("""[]""")
      }
    }
}
