package kairo.serialization

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class AllowUnknownSerializationTest {
  internal data object DataObject

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
  fun `deserialize, default`(): Unit =
    runTest {
      val json = KairoJson()

      shouldThrowExactly<UnrecognizedPropertyException> {
        json.deserialize<DataObject>("""{"other":"unknown"}""")
      }.message.shouldStartWith(
        "Unrecognized field \"other\"",
      )

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
  fun `deserialize, allowUnknown`(): Unit =
    runTest {
      val json = KairoJson {
        allowUnknown = true
      }

      json.deserialize<DataObject>("""{"other":"unknown"}""")
        .shouldBe(DataObject)

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
      ).shouldBe(
        DataClass(
          boolean = true,
          ints = listOf(1, 2, 3),
          nested = DataClass.Nested("Hello, World!"),
        ),
      )
    }
}
