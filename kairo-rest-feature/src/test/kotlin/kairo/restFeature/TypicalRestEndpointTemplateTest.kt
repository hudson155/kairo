package kairo.restFeature

import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Test

/**
 * This test uses [TypicalLibraryBookApi] to test typical (happy path) cases.
 */
internal class TypicalRestEndpointTemplateTest {
  @Test
  fun get() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Get::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun listAll() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.ListAll::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun searchByIsbn() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByIsbn::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(
            RestEndpointQuery.Param("isbn", required = true),
          ),
          contentType = null,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun searchByTitle() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByText::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(
            RestEndpointQuery.Param("title", required = false),
            RestEndpointQuery.Param("author", required = false),
          ),
          contentType = null,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun create() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Create::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Post,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(),
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun update() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Update::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Patch,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun delete() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Delete::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Delete,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = ContentType.Application.Json,
        ),
      )
  }
}
