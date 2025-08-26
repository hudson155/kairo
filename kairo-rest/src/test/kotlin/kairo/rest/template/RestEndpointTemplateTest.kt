package kairo.rest.template

import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.libraryBook.LibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RestEndpointTemplateTest {
  @Test
  fun get(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Get::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
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
  fun listByIds(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.ListByIds::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(
              RestEndpointTemplateQuery.Param("libraryBookIds", required = true),
            ),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun listAll(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.ListAll::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun searchByIsbn(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByIsbn::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(
              RestEndpointTemplateQuery.Param("isbn", required = true),
            ),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun searchByText(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByText::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(
              RestEndpointTemplateQuery.Param("title", required = false),
              RestEndpointTemplateQuery.Param("author", required = false),
            ),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun create(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Create::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Post,
            path = RestEndpointTemplatePath(
              RestEndpointTemplatePath.Component.Constant("library-books"),
            ),
            query = RestEndpointTemplateQuery(),
            contentType = ContentType.Application.Json,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun update(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Update::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Patch,
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
  fun delete(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Delete::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Delete,
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
