package kairo.serialization

import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
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
      }.message.shouldStartWith(
        "Missing required creator property 'boolean'",
      )
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
      }.message.shouldStartWith(
        "Unrecognized field \"other\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<DataClass>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.DataClassSerializationTest.DataClass(non-null) but was null",
      )

      json.deserialize<DataClass?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("true")
      }.message.shouldStartWith(
        "Cannot construct instance of `kairo.serialization.DataClassSerializationTest\$DataClass`",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("0")
      }.message.shouldStartWith(
        "Cannot construct instance of `kairo.serialization.DataClassSerializationTest\$DataClass`",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidFormatException> {
        json.deserialize<DataClass>("\"\"")
      }.message.shouldStartWith(
        "Cannot coerce empty String (\"\")" +
          " to `kairo.serialization.DataClassSerializationTest\$DataClass` value",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<DataClass>("""[]""")
      }.message.shouldStartWith(
        "Cannot deserialize value of type `kairo.serialization.DataClassSerializationTest\$DataClass`" +
          " from Array value",
      )
    }
}
