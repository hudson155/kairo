package kairo.rest.template

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.LibraryBookApi as LibraryBookApi

/**
 * This test is only concerned with testing existing instances of [RestEndpointTemplate].
 * There are other tests in this package concerned with building instances of it.
 */
@Suppress("NullableToStringCall") // False positive.
internal class RestEndpointTemplateTest {
  @Test
  fun `get, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.Get::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET /library-books/:libraryBookId)",
      )
  }

  @Test
  fun `listAll, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.ListAll::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET /library-books)",
      )
  }

  @Test
  fun `searchByIsbn, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.SearchByIsbn::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET /library-books (isbn))",
      )
  }

  @Test
  fun `searchByTitle, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.SearchByText::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET /library-books (title?, author?))",
      )
  }

  @Test
  fun `create, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.Create::class).toString()
      .shouldBe(
        "RestEndpointTemplate([application/json -> application/json]" +
          " POST /library-books)",
      )
  }

  @Test
  fun `update, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.Update::class).toString()
      .shouldBe(
        "RestEndpointTemplate([application/json -> application/json]" +
          " PATCH /library-books/:libraryBookId)",
      )
  }

  @Test
  fun `delete, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.Delete::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " DELETE /library-books/:libraryBookId)",
      )
  }
}
