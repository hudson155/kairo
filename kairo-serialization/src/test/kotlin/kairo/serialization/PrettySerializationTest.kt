package kairo.serialization

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PrettySerializationTest {
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
  fun `serialize, default`(): Unit =
    runTest {
      val json = KairoJson()

      json.serialize(
        DataClass(
          boolean = true,
          ints = listOf(1, 2, 3),
          nested = DataClass.Nested("Hello, World!"),
        ),
      ).shouldBe(
        """{"boolean":true,"ints":[1,2,3],"nested":{"string":"Hello, World!"}}"""
      )
    }

  @Test
  fun `serialize, pretty`(): Unit =
    runTest {
      val json = KairoJson {
        pretty = true
      }

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
}
