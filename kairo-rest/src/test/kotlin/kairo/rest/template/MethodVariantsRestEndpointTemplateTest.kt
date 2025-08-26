package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.libraryBook.MethodVariantsLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class MethodVariantsRestEndpointTemplateTest {
  @Test
  fun sync(): Unit =
    runTest {
      RestEndpointTemplate.from(MethodVariantsLibraryBookApi.Sync::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.parse("SYNC"),
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
              RestEndpointTemplatePath.Component.Param("libraryBookId"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun missing(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(MethodVariantsLibraryBookApi.Missing::class)
      }.shouldHaveMessage(
        "REST endpoint ${MethodVariantsLibraryBookApi.Missing::class.qualifiedName}:" +
          " Must define @Rest.",
      )
    }

  @Test
  fun empty(): Unit =
    runTest {
      RestEndpointTemplate.from(MethodVariantsLibraryBookApi.Empty::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.parse(""),
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
              RestEndpointTemplatePath.Component.Param("libraryBookId"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }
}
