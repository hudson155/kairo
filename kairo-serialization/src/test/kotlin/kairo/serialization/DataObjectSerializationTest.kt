package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class DataObjectSerializationTest {
  private val json: KairoJson = KairoJson {
    pretty = true
  }

  internal data object DataObject

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(DataObject).shouldBe("""{ }""")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DataObject>("""{ }""").shouldBe(DataObject)
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<DataObject>("""{"other":"unknown"}""")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DataObject>("null")
      }

      json.deserialize<DataObject?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataObject>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataObject>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DataObject>("\"\"")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataObject>("""[]""")
      }
    }
}
