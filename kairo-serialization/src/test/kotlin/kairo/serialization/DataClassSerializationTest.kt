package kairo.serialization

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

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
      shouldThrowAny {
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
      shouldThrowAny {
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
      shouldThrowAny {
        json.deserialize<DataClass>("null")
      }

      json.deserialize<DataClass?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<DataClass>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<DataClass>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<DataClass>("\"\"")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<DataClass>("""[]""")
      }
    }
}
