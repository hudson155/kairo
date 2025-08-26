package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.libraryBook.AcceptVariantsLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class AcceptVariantsRestEndpointTemplateTest {
  @Test
  fun csv(): Unit =
    runTest {
      RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.Csv::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Application.Json,
            accept = ContentType.Text.CSV,
          ),
        )
    }

  @Test
  fun notPresentOnGet(): Unit =
    runTest {
      RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.NotPresentOnGet::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
              RestEndpointTemplatePath.Component.Param("libraryBookId"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = null,
          ),
        )
    }

  @Test
  fun notPresentOnPost(): Unit =
    runTest {
      RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.NotPresentOnPost::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Application.Json,
            accept = null,
          ),
        )
    }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun empty(): Unit =
    runTest {
      RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.Empty::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Any,
          ),
        )
    }

  /**
   * Means "Any" content type.
   */
  @Test
  fun star(): Unit =
    runTest {
      RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.Star::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Any,
          ),
        )
    }

  @Test
  fun malformed(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(AcceptVariantsLibraryBookApi.Malformed::class)
      }.shouldHaveMessage(
        "REST endpoint ${AcceptVariantsLibraryBookApi::class.qualifiedName}.Malformed:" +
          " @Rest.Accept is invalid. Bad Content-Type format: application.",
      )
    }
}
