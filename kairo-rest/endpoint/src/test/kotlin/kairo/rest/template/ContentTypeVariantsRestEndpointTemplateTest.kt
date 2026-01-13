package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.libraryBook.ContentTypeVariantsLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class ContentTypeVariantsRestEndpointTemplateTest {
  @Test
  fun csv(): Unit =
    runTest {
      RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.Csv::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Text.CSV,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun `present on get`(): Unit =
    runTest {
      RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.PresentOnGet::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
              RestEndpointTemplatePath.Component.Param("libraryBookId"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Application.Json,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun `not present on post`(): Unit =
    runTest {
      RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.NotPresentOnPost::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun empty(): Unit =
    runTest {
      RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.Empty::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Any,
            accept = ContentType.Application.Json,
          ),
        )
    }

  /**
   * Means "Any" content type.
   */
  @Test
  fun star(): Unit =
    runTest {
      RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.Star::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Any,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun malformed(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(ContentTypeVariantsLibraryBookApi.Malformed::class)
      }.shouldHaveMessage(
        "REST endpoint ${ContentTypeVariantsLibraryBookApi::class.qualifiedName}.Malformed:" +
          " @Rest.ContentType is invalid. Bad Content-Type format: application.",
      )
    }
}
