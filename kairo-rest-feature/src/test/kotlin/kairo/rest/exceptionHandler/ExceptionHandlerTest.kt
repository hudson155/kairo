package kairo.rest.exceptionHandler

import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.path
import io.mockk.every
import io.mockk.mockk
import kairo.client.KairoClient
import kairo.client.KtorClientMapper
import kairo.client.createKairoClient
import kairo.featureTesting.KairoServerTest
import kairo.serverTesting.TestServer

/**
 * The exception handler tests spin up a real Ktor Server and hit it with requests.
 * [mock] allows tests to have the Server throw custom exceptions.
 *  exceptions are thrown by adjusting [request] to have
 */
internal abstract class ExceptionHandlerTest : KairoServerTest() {
  internal class Server(libraryBookService: LibraryBookService) : TestServer(
    featureUnderTest = ExceptionHandlerTestFeature(libraryBookService),
    supportingFeatures = setOf(
      ExceptionHandlerTestRestFeature(),
    ),
  )

  private val libraryBookService: LibraryBookService = mockk()

  override val server: Server = Server(libraryBookService)

  private val client: KairoClient =
    createKairoClient {
      expectSuccess = false
      defaultRequest {
        url("http://localhost:$exceptionHandlerTestRestPort")
      }
    }

  override suspend fun beforeEach() {
    every { libraryBookService.create(any()) } answers {
      val endpoint = firstArg<ExceptionHandlerLibraryBookApi.Create>()
      val creator = endpoint.body
      return@answers ExceptionHandlerLibraryBookRep(
        title = endpoint.title,
        authors = creator.authors,
        isSeries = endpoint.isSeries,
        type = creator.type,
      )
    }
  }

  protected fun mock(answer: () -> ExceptionHandlerLibraryBookRep) {
    every { libraryBookService.create(any()) } answers { answer() }
  }

  protected suspend fun request(block: HttpRequestBuilder.() -> Unit): Pair<HttpStatusCode, Map<String, Any?>?> {
    val response = client.request {
      method = HttpMethod.Post
      url {
        path("/library-books")
        parameters["title"] = "The Name of the Wind"
        parameters["isSeries"] = true.toString()
      }
      accept(ContentType.Application.Json)
      contentType(ContentType.Application.Json)
      setBody(
        mapOf(
          "authors" to listOf(
            mapOf("type" to "Named", "firstName" to "Patrick", "lastName" to "Rothfuss"),
            mapOf("type" to "Named", "firstName" to "Betsy", "lastName" to "Wollheim"),
          ),
          "type" to "Print",
        ),
      )
      block()
    }
    return Pair(
      first = response.status,
      second = response.bodyAsText().let { string ->
        if (string.isEmpty()) return@let null
        return@let KtorClientMapper.json.readValue(string)
      },
    )
  }
}
