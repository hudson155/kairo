package kairo.rest.writer

import io.kotest.matchers.shouldBe
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import java.util.Optional
import kairo.id.KairoId
import kairo.rest.LibraryBookRep
import kairo.rest.TypicalLibraryBookApi
import kairo.rest.endpoint.RestEndpointDetails
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test verifies that [RestEndpointWriter] can properly write [RestEndpointDetails] instances.
 */
internal class RestEndpointWriterTest {
  @Test
  fun get(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Get(
      libraryBookId = KairoId("library_book", "2eDS1sMt"),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Get,
          path = "/library-books/library_book_2eDS1sMt",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }

  @Test
  fun listAll(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.ListAll
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Get,
          path = "/library-books",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.SearchByIsbn(
      isbn = "978-0756405892",
      strict = true,
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Get,
          path = "/library-books?isbn=978-0756405892&strict=true",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }

  @Test
  fun `searchByText (neither provided)`(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.SearchByText(
      title = null,
      author = null,
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Get,
          path = "/library-books?",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }

  @Test
  fun `searchByText (both provided)`(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.SearchByText(
      title = "The Name of the Wind",
      author = "Patrick Rothfuss",
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Get,
          path = "/library-books?title=The+Name+of+the+Wind&author=Patrick+Rothfuss",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }

  @Test
  fun create(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Create(
      body = LibraryBookRep.Creator(
        title = "The Name of the Wind",
        author = "Patrick Rothfuss",
        isbn = "978-0756405892",
      ),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Post,
          path = "/library-books",
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
          body = LibraryBookRep.Creator(
            title = "The Name of the Wind",
            author = "Patrick Rothfuss",
            isbn = "978-0756405892",
          ),
        ),
      )
  }

  @Test
  fun `update (neither provided)`(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Update(
      libraryBookId = KairoId("library_book", "2eDS1sMt"),
      body = LibraryBookRep.Update(
        title = null,
        author = null,
      ),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Patch,
          path = "/library-books/library_book_2eDS1sMt",
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
          body = LibraryBookRep.Update(
            title = null,
            author = null,
          ),
        ),
      )
  }

  @Test
  fun `update (author null)`(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Update(
      libraryBookId = KairoId("library_book", "2eDS1sMt"),
      body = LibraryBookRep.Update(
        title = null,
        author = Optional.empty(),
      ),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Patch,
          path = "/library-books/library_book_2eDS1sMt",
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
          body = LibraryBookRep.Update(
            title = null,
            author = Optional.empty(),
          ),
        ),
      )
  }

  @Test
  fun `update (both provided)`(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Update(
      libraryBookId = KairoId("library_book", "2eDS1sMt"),
      body = LibraryBookRep.Update(
        title = "The Name of the Wind",
        author = Optional.of("Patrick Rothfuss"),
      ),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Patch,
          path = "/library-books/library_book_2eDS1sMt",
          contentType = ContentType.Application.Json,
          accept = ContentType.Application.Json,
          body = LibraryBookRep.Update(
            title = "The Name of the Wind",
            author = Optional.of("Patrick Rothfuss"),
          ),
        ),
      )
  }

  @Test
  fun delete(): Unit = runTest {
    val endpoint = TypicalLibraryBookApi.Delete(
      libraryBookId = KairoId("library_book", "2eDS1sMt"),
    )
    RestEndpointWriter.from(endpoint::class).write(endpoint)
      .shouldBe(
        RestEndpointDetails(
          method = HttpMethod.Delete,
          path = "/library-books/library_book_2eDS1sMt",
          contentType = null,
          accept = ContentType.Application.Json,
          body = null,
        ),
      )
  }
}
