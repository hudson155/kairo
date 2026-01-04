package kairo.serialization

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kairo.serialization.DataClassSerializationTest.DataClass
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Although the serialization tests for most other well-known types
 * don't include a list serialization test,
 * the polymorphic serialization test does
 * so that we can test generic type erasure.
 */
internal class PolymorphicSerializationTest {
  private val json: KairoJson = KairoJson {
    pretty = true
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(Animal.Dog::class, name = "Dog"),
    JsonSubTypes.Type(Animal.Cat::class, name = "Cat"),
  )
  internal sealed class Animal {
    abstract val name: String

    internal data class Dog(override val name: String, val barksPerMinute: Int) : Animal()

    internal data class Cat(override val name: String, val napsPerDay: Int) : Animal()
  }

  @Test
  fun serialize(): Unit =
    runTest {
      json.serialize<Animal.Dog>(Animal.Dog("Rex", 30))
        .shouldBe(
          """
            {
              "type": "Dog",
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )

      json.serialize<Animal.Cat>(Animal.Cat("Whiskers", 12))
        .shouldBe(
          """
            {
              "type": "Cat",
              "name": "Whiskers",
              "napsPerDay": 12
            }
          """.trimIndent(),
        )
    }

  @Test
  fun `serialize (base class)`(): Unit =
    runTest {
      json.serialize<Animal>(Animal.Dog("Rex", 30))
        .shouldBe(
          """
            {
              "type": "Dog",
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )

      json.serialize<Animal>(Animal.Cat("Whiskers", 12))
        .shouldBe(
          """
            {
              "type": "Cat",
              "name": "Whiskers",
              "napsPerDay": 12
            }
          """.trimIndent(),
        )
    }

  @Test
  fun `serialize, list`(): Unit =
    runTest {
      json.serialize<List<Animal.Dog>>(listOf(Animal.Dog("Rex", 30)))
        .shouldBe(
          """
            [
              {
                "type": "Dog",
                "name": "Rex",
                "barksPerMinute": 30
              }
            ]
          """.trimIndent(),
        )

      json.serialize<List<Animal.Cat>>(listOf(Animal.Cat("Whiskers", 12)))
        .shouldBe(
          """
            [
              {
                "type": "Cat",
                "name": "Whiskers",
                "napsPerDay": 12
              }
            ]
          """.trimIndent(),
        )
    }

  @Test
  fun `serialize, list (base class)`(): Unit =
    runTest {
      json.serialize<List<Animal>>(listOf(Animal.Dog("Rex", 30), Animal.Cat("Whiskers", 12)))
        .shouldBe(
          """
            [
              {
                "type": "Dog",
                "name": "Rex",
                "barksPerMinute": 30
              },
              {
                "type": "Cat",
                "name": "Whiskers",
                "napsPerDay": 12
              }
            ]
          """.trimIndent(),
        )
    }

  @Test
  fun `deserialize, missing discriminator`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>(
          """
            {
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )
      }
    }

  @Test
  fun `deserialize, unknown discriminator`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>(
          """
            {
              "type": "Fish",
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )
      }
    }

  @Test
  fun `deserialize, missing property`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>(
          """
            {
              "type": "Dog",
              "name": "Rex"
            }
          """.trimIndent(),
        )
      }
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>(
          """
            {
              "type": "Dog",
              "name": "Rex",
              "barksPerMinute": 30,
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
        json.deserialize<Animal>("null")
      }

      json.deserialize<Animal?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>("true")
      }
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>("0")
      }
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>("\"\"")
      }
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowAny {
        json.deserialize<Animal>("""[]""")
      }
    }
}
