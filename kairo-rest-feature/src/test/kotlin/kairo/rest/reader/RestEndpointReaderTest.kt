package kairo.rest.reader

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.http.Parameters
import io.ktor.server.routing.RoutingCall
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.lang.reflect.InvocationTargetException
import java.util.Optional
import kairo.id.KairoId
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.TypicalLibraryBookApi as LibraryBookApi
import kairo.rest.TypicalLibraryBookRep as LibraryBookRep

/**
 * This test verifies that [RestEndpointReader] can properly derive [RestEndpoint] instances from [RoutingCall]s.
 *
 * Note that for bodies, this test serializes maps and lists instead of data classes
 * in order to avoid symmetrical unexpected serialization issues
 * and test non-happy paths.
 */
internal class RestEndpointReaderTest {
  @Test
  fun `get, invalid path param`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Get::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "2eDS1sMt")
      }
    }
    shouldThrow<IllegalArgumentException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `get, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Get::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Get(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
        ),
      )
  }

  @Test
  fun `listAll, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.ListAll::class)
    val call = mockk<RoutingCall>()
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.ListAll,
      )
  }

  @Test
  fun `searchByIsbn, missing isbn`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.SearchByIsbn::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.Empty
    }
    shouldThrow<InvocationTargetException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `searchByIsbn, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.SearchByIsbn::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("isbn", "978-0756405892")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.SearchByIsbn(
          isbn = "978-0756405892",
        ),
      )
  }

  @Test
  fun `searchByText, happy path (neither provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.SearchByText::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.Empty
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.SearchByText(
          title = null,
          author = null,
        ),
      )
  }

  @Test
  fun `searchByText, happy path (both provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.SearchByText::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("title", "The Name of the Wind")
        append("author", "Patrick Rothfuss")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.SearchByText(
          title = "The Name of the Wind",
          author = "Patrick Rothfuss",
        ),
      )
  }

  @Test
  fun `create, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Create::class)
    val call = mockk<RoutingCall> {
      coEvery {
        receiveNullable<LibraryBookRep.Creator>(any())
      } returns LibraryBookRep.Creator(
        title = "The Name of the Wind",
        author = "Patrick Rothfuss",
        isbn = "978-0756405892",
      )
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Create(
          body = LibraryBookRep.Creator(
            title = "The Name of the Wind",
            author = "Patrick Rothfuss",
            isbn = "978-0756405892",
          ),
        ),
      )
  }

  @Test
  fun `update, happy path (neither provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
      coEvery {
        receiveNullable<LibraryBookRep.Update>(any())
      } returns LibraryBookRep.Update(
        title = null,
        author = null,
      )
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Update(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
          body = LibraryBookRep.Update(
            title = null,
            author = null,
          ),
        ),
      )
  }

  @Test
  fun `update, happy path (author null)`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
      coEvery {
        receiveNullable<LibraryBookRep.Update>(any())
      } returns LibraryBookRep.Update(
        title = null,
        author = Optional.empty(),
      )
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Update(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
          body = LibraryBookRep.Update(
            title = null,
            author = Optional.empty(),
          ),
        ),
      )
  }

  @Test
  fun `update, happy path (both provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
      coEvery {
        receiveNullable<LibraryBookRep.Update>(any())
      } returns LibraryBookRep.Update(
        title = "The Name of the Wind",
        author = Optional.of("Patrick Rothfuss"),
      )
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Update(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
          body = LibraryBookRep.Update(
            title = "The Name of the Wind",
            author = Optional.of("Patrick Rothfuss"),
          ),
        ),
      )
  }

  @Test
  fun `delete, invalid path param`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Delete::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "2eDS1sMt")
      }
    }
    shouldThrow<IllegalArgumentException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `delete, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(LibraryBookApi.Delete::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        LibraryBookApi.Delete(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
        ),
      )
  }
}
