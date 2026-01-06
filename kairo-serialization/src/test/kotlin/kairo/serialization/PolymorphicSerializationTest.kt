package kairo.serialization

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.RuntimeJsonMappingException
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Although the serialization tests for most other well-known types
 * don't include a list serialization test,
 * the polymorphic serialization test does
 * so that we can test generic type erasure.
 */
internal class PolymorphicSerializationTest {
  private val json: KairoJson =
    KairoJson {
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
      shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>(
          """
            {
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )
      }.message.shouldStartWith(
        "Could not resolve subtype of [simple type, class kairo.serialization.PolymorphicSerializationTest\$Animal]",
      )
    }

  @Test
  fun `deserialize, unknown discriminator`(): Unit =
    runTest {
      shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>(
          """
            {
              "type": "Fish",
              "name": "Rex",
              "barksPerMinute": 30
            }
          """.trimIndent(),
        )
      }.message.shouldStartWith(
        "Could not resolve type id 'Fish' as a subtype of `kairo.serialization.PolymorphicSerializationTest\$Animal`",
      )
    }

  @Test
  fun `deserialize, missing property`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Animal>(
          """
            {
              "type": "Dog",
              "name": "Rex"
            }
          """.trimIndent(),
        )
      }.message.shouldStartWith(
        "Missing required creator property 'barksPerMinute'",
      )
    }

  @Test
  fun `deserialize, unknown property`(): Unit =
    runTest {
      shouldThrowExactly<UnrecognizedPropertyException> {
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
      }.message.shouldStartWith(
        "Unrecognized field \"other\"",
      )
    }

  @Test
  fun `deserialize, null`(): Unit =
    runTest {
      shouldThrowExactly<RuntimeJsonMappingException> {
        json.deserialize<Animal>("null")
      }.message.shouldStartWith(
        "Deserialized value did not match the specified type" +
          "; specified kairo.serialization.PolymorphicSerializationTest.Animal(non-null) but was null",
      )

      json.deserialize<Animal?>("null").shouldBeNull()
    }

  @Test
  fun `deserialize, wrong type (boolean)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>("true")
      }.message.shouldStartWith(
        "Could not resolve subtype of [simple type, class kairo.serialization.PolymorphicSerializationTest\$Animal]",
      )
    }

  @Test
  fun `deserialize, wrong type (int)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>("0")
      }.message.shouldStartWith(
        "Could not resolve subtype of [simple type, class kairo.serialization.PolymorphicSerializationTest\$Animal]",
      )
    }

  @Test
  fun `deserialize, wrong type (string)`(): Unit =
    runTest {
      shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>("\"\"")
      }.message.shouldStartWith(
        "Could not resolve subtype of [simple type, class kairo.serialization.PolymorphicSerializationTest\$Animal]",
      )
    }

  @Test
  fun `deserialize, wrong type (array)`(): Unit =
    runTest {
      shouldThrowExactly<MismatchedInputException> {
        json.deserialize<Animal>("""[]""")
      }.message.shouldStartWith(
        "Unexpected token (END_ARRAY)",
      )
    }
}
