package kairo.rest.template

import io.kotest.matchers.shouldBe
import kairo.rest.LibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RestEndpointTemplateToStringTest {
  @Test
  fun get(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Get::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[null -> application/json]" +
            " GET /library-books/:libraryBookId')",
        )
    }

  @Test
  fun listAll(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.ListAll::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[null -> application/json]" +
            " GET /library-books')",
        )
    }

  @Test
  fun searchByIsbn(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByIsbn::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[null -> application/json]" +
            " GET /library-books (isbn)')",
        )
    }

  @Test
  fun searchByTitle(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByText::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[null -> application/json]" +
            " GET /library-books (title?, author?)')",
        )
    }

  @Test
  fun create(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Create::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[application/json -> application/json]" +
            " POST /library-books')",
        )
    }

  @Test
  fun update(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Update::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[application/json -> application/json]" +
            " PATCH /library-books/:libraryBookId')",
        )
    }

  @Test
  fun delete(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Delete::class).toString()
        .shouldBe(
          "RestEndpointTemplate(value='[null -> application/json]" +
            " DELETE /library-books/:libraryBookId')",
        )
    }
}
