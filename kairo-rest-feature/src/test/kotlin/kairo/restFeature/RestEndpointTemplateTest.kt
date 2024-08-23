package kairo.restFeature

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * This test is only concerned with testing existing instances of [RestEndpointTemplate].
 * There are other tests in this package concerned with building instances of it.
 */
@Suppress("NullableToStringCall") // False positive.
internal class RestEndpointTemplateTest {
  @Test
  fun `get, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Get::class).toString()
      .shouldBe("[ -> application/json] GET library/library-books/:libraryBookId")
  }

  @Test
  fun `listAll, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.ListAll::class).toString()
      .shouldBe("[ -> application/json] GET library/library-books")
  }

  @Test
  fun `searchByIsbn, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByIsbn::class).toString()
      .shouldBe("[ -> application/json] GET library/library-books (isbn)")
  }

  @Test
  fun `searchByTitle, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByText::class).toString()
      .shouldBe("[ -> application/json] GET library/library-books (title?, author?)")
  }

  @Test
  fun `create, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Create::class).toString()
      .shouldBe("[application/json -> application/json] POST library/library-books")
  }

  @Test
  fun `update, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Update::class).toString()
      .shouldBe("[application/json -> application/json] PATCH library/library-books/:libraryBookId")
  }

  @Test
  fun `delete, toString method`() {
    RestEndpointTemplate.parse(TypicalLibraryBookApi.Delete::class).toString()
      .shouldBe("[ -> application/json] DELETE library/library-books/:libraryBookId")
  }
}
