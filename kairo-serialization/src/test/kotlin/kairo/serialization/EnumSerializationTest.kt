package kairo.serialization

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class EnumSerializationTest {
  internal enum class Genre {
    Fantasy,
    History,
    Religion,
    Romance,
    Science,
    ScienceFiction,

    /**
     * [JsonEnumDefaultValue] is used to ensure that Jackson does NOT use it for deserialization.
     */
    @JsonEnumDefaultValue
    Default,
    ;

    /**
     * [toString] is implemented to ensure that Jackson does NOT use it for serialization.
     */
    override fun toString(): String =
      name.filter { it.isUpperCase() }
  }

  private val json: KairoJson = KairoJson()

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(Genre.Science).shouldBe("\"Science\"")
      json.serialize(Genre.ScienceFiction).shouldBe("\"ScienceFiction\"")
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<Genre>("\"Science\"").shouldBe(Genre.Science)
      json.deserialize<Genre>("\"ScienceFiction\"").shouldBe(Genre.ScienceFiction)
    }

  @Test
  fun `deserialize, unknown enum`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Genre>("\"Education\"")
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Genre>("null")
      }

      json.deserialize<Genre?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Genre>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<Genre>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (object)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Genre>("""{}""")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Genre>("""[]""")
      }
    }
}
