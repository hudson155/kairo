package kairo.vertexAi.schema

import com.google.genai.Client
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.ContentType
import kairo.vertexAi.VertexAiClientFactory
import kairo.vertexAi.dsl.generateContentConfig
import kairo.vertexAi.dsl.systemInstruction
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class StructuredOutputEval {
  @Serializable
  internal data class PersonSchema(
    val fullName: String,
    val worksAtHighbeam: Boolean,
    val province: Province,
  ) {
    internal enum class Province {
      BritishColumbia,
      Alberta,
      Saskatchewan,
      Manitoba,
      Ontario,
      Quebec,
      NewBrunswick,
      PrinceEdwardIsland,
      NovaScotia,
      NewfoundlandAndLabrador,
    }
  }

  @Serializable
  internal sealed class AnimalSchema {
    internal abstract val name: String

    @Serializable
    @SerialName("Dog")
    internal data class DogSchema(override val name: String, val barksPerMinute: Int) : AnimalSchema()

    @Serializable
    @SerialName("Cat")
    internal data class CatSchema(override val name: String, val napsPerDay: Int) : AnimalSchema()
  }

  private val client: Client = VertexAiClientFactory.fromEnvironment()

  @Test
  fun person(): Unit =
    runTest {
      val input = """
        Jeff Hudson is a software engineer at Highbeam.
        He is 45 years old.
        He lives in Alberta.
      """.trimIndent()
      val response = client.models.generateContent(
        "gemini-2.5-flash-lite",
        input,
        generateContentConfig {
          systemInstruction("Respond with metadata about the person the user describes.")
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<PersonSchema>())
        },
      )
      val person = Json.decodeFromString<PersonSchema>(response.text().shouldNotBeNull())
      person.shouldBe(
        PersonSchema(
          fullName = "Jeff Hudson",
          worksAtHighbeam = true,
          province = PersonSchema.Province.Alberta,
        ),
      )
    }

  @Test
  fun `format, email`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Format("email")
        val value: String,
      )

      val input = "Provide random fixture data."
      val response = client.models.generateContent(
        "gemini-2.5-flash-lite",
        input,
        generateContentConfig {
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<TestSchema>())
        },
      )
      val value = Json.decodeFromString<TestSchema>(response.text().shouldNotBeNull()).value
      value.shouldContain("@")
    }

  @Test
  fun `number, range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Description("Provide the lowest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val first: Int,
        @Vertex.Description("Provide the lowest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val second: Long,
        @Vertex.Description("Provide the lowest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val third: Float,
        @Vertex.Description("Provide the lowest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val fourth: Double,
        @Vertex.Description("Provide the highest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val fifth: Int,
        @Vertex.Description("Provide the highest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val sixth: Long,
        @Vertex.Description("Provide the highest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val seventh: Float,
        @Vertex.Description("Provide the highest allowed value.")
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val eighth: Double,
      )

      val input = "Provide data."
      val response = client.models.generateContent(
        "gemini-2.5-flash-lite",
        input,
        generateContentConfig {
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<TestSchema>())
        },
      )
      val value = Json.decodeFromString<TestSchema>(response.text().shouldNotBeNull())
      value.shouldBe(
        TestSchema(
          first = 1,
          second = 1L,
          third = 1.0F,
          fourth = 1.0,
          fifth = 7,
          sixth = 7L,
          seventh = 7.0F,
          eighth = 7.0,
        ),
      )
    }

  @Test
  fun `array, range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Description("Provide as many baby names as possible.")
        @Vertex.Min(1.0)
        @Vertex.Max(3.0)
        val babyNames: List<String>,
        @Vertex.Description("Provide as few error messages as possible.")
        @Vertex.Min(1.0)
        @Vertex.Max(3.0)
        val errorMessages: List<String>,
      )

      val input = "Provide data."
      val response = client.models.generateContent(
        "gemini-2.5-flash", // Other tests use lite, but lite doesn't work reliably with array ranges.
        input,
        generateContentConfig {
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<TestSchema>())
        },
      )
      val value = Json.decodeFromString<TestSchema>(response.text().shouldNotBeNull())
      value.babyNames.shouldHaveSize(3)
      value.errorMessages.shouldHaveSize(1)
    }

  @Test
  fun polymorphic(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(3.0)
        val cat: AnimalSchema,
        @Vertex.Min(1.0)
        @Vertex.Max(3.0)
        val dog: AnimalSchema,
      )

      val input = "Provide random fixture data."
      val response = client.models.generateContent(
        "gemini-2.5-flash-lite",
        input,
        generateContentConfig {
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<TestSchema>())
        },
      )
      val json = Json.decodeFromString<TestSchema>(response.text().shouldNotBeNull())
      json.cat.shouldBeInstanceOf<AnimalSchema.CatSchema>()
      json.dog.shouldBeInstanceOf<AnimalSchema.DogSchema>()
    }
}
