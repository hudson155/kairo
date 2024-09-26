package kairo.rest.exceptionHandler

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
import kairo.client.createKairoClient
import kairo.featureTesting.KairoServerTest
import kairo.serverTesting.TestServer
import org.junit.jupiter.api.BeforeEach

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
      defaultRequest {
        url("http://localhost:$exceptionHandlerTestRestPort")
      }
    }

  @BeforeEach
  override fun beforeEach() {
    super.beforeEach()

    every { libraryBookService.create(any()) } answers {
      val endpoint = firstArg<ExceptionHandlerLibraryBookApi.Create>()
      val creator = endpoint.body
      return@answers ExceptionHandlerLibraryBookRep(
        title = endpoint.title,
        authors = creator.authors,
        type = creator.type,
      )
    }
  }

  protected fun mock(answer: () -> ExceptionHandlerLibraryBookRep?) {
    every { libraryBookService.create(any()) } answers { answer() }
  }

  protected suspend fun request(block: HttpRequestBuilder.() -> Unit): Pair<HttpStatusCode, String> {
    val response = client.request {
      method = HttpMethod.Post
      url {
        path("/library-books")
        parameters["title"] = "The Name of the Wind"
      }
      accept(ContentType.Application.Json)
      contentType(ContentType.Application.Json)
      setBody(
        """
          {
            "authors": [
              { "type": "Named", "firstName": "Patrick", "lastName": "Rothfuss" },
              { "type": "Named", "firstName": "Betsy", "lastName": "Wollheim" }
            ],
            "type": "Print"
          }
        """.trimIndent(),
      )
      block()
    }
    return Pair(response.status, response.bodyAsText())
  }
}