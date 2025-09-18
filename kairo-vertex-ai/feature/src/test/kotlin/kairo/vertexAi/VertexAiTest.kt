package kairo.vertexAi

import com.google.genai.Client
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.http.ContentType
import kairo.vertexAi.dsl.generateContentConfig
import kairo.vertexAi.dsl.systemInstruction
import kairo.vertexAi.schema.Vertex
import kairo.vertexAi.schema.VertexSchemaGenerator
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class VertexAiTest {
  @Serializable
  internal data class Eval(
    @Vertex.Description("A brief description of your correctness assessment.")
    val reason: String, // Reason is before score as an attention trick.
    @Vertex.Description("Whether the model was correct.")
    val correct: Boolean,
  )

  private val client: Client = run {
    System.getenv("GCP_PROJECT").let { println("GCP_PROJECT: $it") }
    System.getenv("GCP_LOCATION").let { println("GCP_LOCATION: $it") }
    VertexAiClientFactory.fromEnvironment()
  }

  @Test
  fun test(): Unit =
    runTest {
      val input = "What's the capital of Alberta?"
      val response = client.models.generateContent("gemini-2.5-flash-lite", input, generateContentConfig())
      val responseText = response.text().shouldNotBeNull()
      val evalResponse = client.models.generateContent(
        "gemini-2.5-flash-lite",
        evalPrompt(input, responseText),
        generateContentConfig {
          systemInstruction("The correct answer is Alberta. Respond whether the model was correct.")
          responseMimeType(ContentType.Application.Json.toString())
          responseSchema(VertexSchemaGenerator.generate<Eval>())
        },
      )
      val eval = Json.decodeFromString<Eval>(evalResponse.text().shouldNotBeNull())
      eval.correct.shouldBeTrue()
    }
}
