package kairo.rest

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test is only concerned with testing existing instances of [RestEndpointTemplate].
 * There are other tests in this package concerned with building instances of it.
 */
@Suppress("NullableToStringCall") // False positive.
internal class RestEndpointTemplateTest {
  @Test
  fun `get, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.Get::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET library/library-books/:libraryBookId)",
      )
  }

  @Test
  fun `listAll, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.ListAll::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET library/library-books)",
      )
  }

  @Test
  fun `searchByIsbn, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByIsbn::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET library/library-books (isbn))",
      )
  }

  @Test
  fun `searchByTitle, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByText::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " GET library/library-books (title?, author?))",
      )
  }

  @Test
  fun `create, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.Create::class).toString()
      .shouldBe(
        "RestEndpointTemplate([application/json -> application/json]" +
          " POST library/library-books)",
      )
  }

  @Test
  fun `update, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.Update::class).toString()
      .shouldBe(
        "RestEndpointTemplate([application/json -> application/json]" +
          " PATCH library/library-books/:libraryBookId)",
      )
  }

  @Test
  fun `delete, toString method`(): Unit = runTest {
    RestEndpointTemplate.from(TypicalLibraryBookApi.Delete::class).toString()
      .shouldBe(
        "RestEndpointTemplate([ -> application/json]" +
          " DELETE library/library-books/:libraryBookId)",
      )
  }
}
