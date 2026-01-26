package kairo.rest.exception

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.HttpStatusCode
import kairo.serialization.KairoJson
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class InvalidTypeIdExceptionTest {
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

  internal data class Nested(
    val animals: List<Animal>,
  )

  private val json: KairoJson = KairoJson()

  @Test
  fun simple(): Unit =
    runTest {
      val string = """
        {
          "type": "Fish",
          "name": "Rex",
          "barksPerMinute": 30
        }
      """.trimIndent()
      val e = shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Animal>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "UnrecognizedTypeDiscriminator",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Unrecognized type discriminator",
            "detail" to null,
            "path" to "/",
            "discriminator" to "Fish",
          ),
        )
    }

  @Test
  fun nested(): Unit =
    runTest {
      val string = """
        {
          "animals": [
            {
              "type": "Dog",
              "name": "Rex",
              "barksPerMinute": 30
            },
            {
              "type": "Fish",
              "name": "Rex",
              "barksPerMinute": 30
            }
          ]
        }
      """.trimIndent()
      val e = shouldThrowExactly<InvalidTypeIdException> {
        json.deserialize<Nested>(string)
      }
      e.toLogicalFailure().shouldNotBeNull().json
        .shouldContainExactly(
          mapOf(
            "type" to "UnrecognizedTypeDiscriminator",
            "status" to HttpStatusCode.BadRequest,
            "message" to "Unrecognized type discriminator",
            "detail" to null,
            "path" to "/animals/1",
            "discriminator" to "Fish",
          ),
        )
    }
}
