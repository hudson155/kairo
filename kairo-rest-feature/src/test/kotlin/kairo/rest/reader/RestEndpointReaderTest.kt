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
import kairo.rest.LibraryBookRep
import kairo.rest.TypicalLibraryBookApi
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

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
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Get::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "2eDS1sMt")
      }
    }
    shouldThrow<RestEndpointParamException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `get, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Get::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.Get(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
        ),
      )
  }

  @Test
  fun `listAll, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.ListAll::class)
    val call = mockk<RoutingCall>()
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.ListAll,
      )
  }

  @Test
  fun `searchByIsbn, missing isbn`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.SearchByIsbn::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("strict", "true")
      }
    }
    shouldThrow<InvocationTargetException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `searchByIsbn, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.SearchByIsbn::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("isbn", "978-0756405892")
        append("strict", "true")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.SearchByIsbn(
          isbn = "978-0756405892",
          strict = true,
        ),
      )
  }

  @Test
  fun `searchByText, happy path (neither provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.SearchByText::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.Empty
    }
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.SearchByText(
          title = null,
          author = null,
        ),
      )
  }

  @Test
  fun `searchByText, happy path (both provided)`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.SearchByText::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("title", "The Name of the Wind")
        append("author", "Patrick Rothfuss")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.SearchByText(
          title = "The Name of the Wind",
          author = "Patrick Rothfuss",
        ),
      )
  }

  @Test
  fun `create, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Create::class)
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
        TypicalLibraryBookApi.Create(
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
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Update::class)
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
        TypicalLibraryBookApi.Update(
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
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Update::class)
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
        TypicalLibraryBookApi.Update(
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
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Update::class)
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
        TypicalLibraryBookApi.Update(
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
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Delete::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "2eDS1sMt")
      }
    }
    shouldThrow<RestEndpointParamException> {
      reader.endpoint(call)
    }
  }

  @Test
  fun `delete, happy path`(): Unit = runTest {
    val reader = RestEndpointReader.from(TypicalLibraryBookApi.Delete::class)
    val call = mockk<RoutingCall> {
      every { parameters } returns Parameters.build {
        append("libraryBookId", "library_book_2eDS1sMt")
      }
    }
    reader.endpoint(call)
      .shouldBe(
        TypicalLibraryBookApi.Delete(
          libraryBookId = KairoId("library_book", "2eDS1sMt"),
        ),
      )
  }
}
