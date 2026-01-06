package kairo.serialization

import com.fasterxml.jackson.databind.exc.InvalidFormatException

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException

import com.fasterxml.jackson.databind.exc.MismatchedInputException

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test does NOT attempt to comprehensively include other well-known types in [DataClass].
 */
internal class DataClassSerializationTest {
  private val json: KairoJson = KairoJson {
    pretty = true
  }

  internal data class DataClass(
    val boolean: Boolean,
    val ints: List<Int>,
    val nested: Nested,
  ) {
    internal data class Nested(
      val string: String,
    )
  }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize(
        DataClass(
          boolean = true,
          ints = listOf(1, 2, 3),
          nested = DataClass.Nested("Hello, World!"),
        ),
      ).shouldBe(
        """
          {
            "boolean": true,
            "ints": [
              1,
              2,
              3
            ],
            "nested": {
              "string": "Hello, World!"
            }
          }
        """.trimIndent(),
      )
    }

  @Test
  fun deserialize(): Unit =
    runTest {
      json.deserialize<DataClass>(
        """
          {
            "boolean": true,
            "ints": [
              1,
              2,
              3
            ],
            "nested": {
              "string": "Hello, World!"
            }
          }
        """.trimIndent(),
      ).shouldBe(
        DataClass(
          boolean = true,
          ints = listOf(1, 2, 3),
          nested = DataClass.Nested("Hello, World!"),
        ),
      )
    }

  @Test
  fun `deserialize, missing property`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>(
          """
            {
              "ints": [
                1,
                2,
                3
              ],
              "nested": {
                "string": "Hello, World!"
              }
            }
          """.trimIndent(),
        )
      }
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<DataClass>(
          """
            {
              "boolean": true,
              "ints": [
                1,
                2,
                3
              ],
              "nested": {
                "string": "Hello, World!"
              },
              "other": "unknown"
            }
          """.trimIndent(),
        )
      }
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DataClass>("null")
      }

      json.deserialize<DataClass?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DataClass>("\"\"")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("""[]""")
      }
    }
}
