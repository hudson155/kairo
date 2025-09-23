package kairo.vertexAi.schema

import com.google.genai.Client
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.ktor.http.ContentType
import kairo.vertexAi.VertexAiClientFactory
import kairo.vertexAi.dsl.generateContentConfig
import kairo.vertexAi.dsl.systemInstruction
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class StructuredOutputEval {
  private val client: Client = VertexAiClientFactory.fromEnvironment()

  @Test
  fun person(): Unit =
    runTest {
      @Serializable
      data class PersonSchema(
        val fullName: String,
        val worksAtHighbeam: Boolean,
      )

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
  fun range(): Unit =
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
}
