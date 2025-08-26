package kairo.rest.reader

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.Parameters
import io.ktor.server.routing.RoutingCall
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kairo.libraryBook.LibraryBookApi
import kairo.libraryBook.LibraryBookId
import kairo.libraryBook.LibraryBookRep
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test

internal class RestEndpointReaderTest {
  @Test
  fun `get, missing path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Get::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `get, malformed path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Get::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "2eDS1sMt")
        }
      }
      shouldThrow<IllegalArgumentException> {
        reader.read(call)
      }.shouldHaveMessage("Malformed library book ID (value=2eDS1sMt).")
    }

  @Test
  fun `get, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Get::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "library_book_2eDS1sMt")
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Get(
            libraryBookId = LibraryBookId("library_book_2eDS1sMt"),
          ),
        )
    }

  @Test
  fun `listByIds (non-empty)`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.ListByIds::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.ListByIds(
            libraryBookIds = emptyList(),
          ),
        )
    }

  @Test
  fun `listByIds (empty)`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.ListByIds::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          appendAll("libraryBookIds", listOf("library_book_2eDS1sMt", "library_book_X64k1rU2"))
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.ListByIds(
            libraryBookIds = listOf(
              LibraryBookId("library_book_2eDS1sMt"),
              LibraryBookId("library_book_X64k1rU2"),
            ),
          ),
        )
    }

  @Test
  fun `listAll, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.ListAll::class)
      val call = mockk<RoutingCall>()
      reader.read(call)
        .shouldBe(
          LibraryBookApi.ListAll,
        )
    }

  @Test
  fun `searchByGenre, missing genre`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByGenre::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `searchByGenre, malformed genre`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByGenre::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("genre", "Christianity")
        }
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `searchByGenre, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByGenre::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("genre", "Religion")
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.SearchByGenre(
            genre = LibraryBookRep.Genre.Religion,
          ),
        )
    }

  @Test
  fun `searchByIsbn, missing isbn`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByIsbn::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `searchByIsbn, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByIsbn::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("isbn", "978-0060652920")
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.SearchByIsbn(
            isbn = "978-0060652920",
          ),
        )
    }

  @Test
  fun `searchByText, none provided`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByText::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.SearchByText(
            title = null,
            author = null,
          ),
        )
    }

  @Test
  fun `searchByText, all provided`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.SearchByText::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("title", "Mere Christianity")
          append("author", "C. S. Lewis")
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.SearchByText(
            title = "Mere Christianity",
            author = "C. S. Lewis",
          ),
        )
    }

  // TODO: Test error handling for bodies. Probably in a different spot.

  @Test
  fun `create, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Create::class)
      val call = mockk<RoutingCall> {
        coEvery {
          receiveNullable<LibraryBookRep.Creator>(any())
        } returns LibraryBookRep.Creator(
          title = "Mere Christianity",
          authors = listOf("C. S. Lewis"),
          isbn = "978-0060652920",
          genre = LibraryBookRep.Genre.Religion,
        )
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Create(
            body = LibraryBookRep.Creator(
              title = "Mere Christianity",
              authors = listOf("C. S. Lewis"),
              isbn = "978-0060652920",
              genre = LibraryBookRep.Genre.Religion,
            ),
          ),
        )
    }

  @Test
  fun `update, missing path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
        coEvery {
          receiveNullable<LibraryBookRep.Update>(any())
        } returns LibraryBookRep.Update(
          title = "The Meaning of Marriage: Facing the Complexities of Commitment with the Wisdom of God",
          authors = listOf("Timothy Keller", "Kathy Keller"),
          genre = LibraryBookRep.Genre.Religion,
        )
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `update, malformed path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "2eDS1sMt")
        }
        coEvery {
          receiveNullable<LibraryBookRep.Update>(any())
        } returns LibraryBookRep.Update(
          title = "The Meaning of Marriage: Facing the Complexities of Commitment with the Wisdom of God",
          authors = listOf("Timothy Keller", "Kathy Keller"),
          genre = LibraryBookRep.Genre.Religion,
        )
      }
      shouldThrow<IllegalArgumentException> {
        reader.read(call)
      }.shouldHaveMessage("Malformed library book ID (value=2eDS1sMt).")
    }

  @Test
  fun `update, none provided`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, title null`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, authors empty`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, all provided`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Update::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "library_book_2eDS1sMt")
        }
        coEvery {
          receiveNullable<LibraryBookRep.Update>(any())
        } returns LibraryBookRep.Update(
          title = "The Meaning of Marriage: Facing the Complexities of Commitment with the Wisdom of God",
          authors = listOf("Timothy Keller", "Kathy Keller"),
          genre = LibraryBookRep.Genre.Religion,
        )
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Update(
            libraryBookId = LibraryBookId("library_book_2eDS1sMt"),
            body = LibraryBookRep.Update(
              title = "The Meaning of Marriage: Facing the Complexities of Commitment with the Wisdom of God",
              authors = listOf("Timothy Keller", "Kathy Keller"),
              genre = LibraryBookRep.Genre.Religion,
            ),
          ),
        )
    }

  @Test
  fun `delete, missing path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Delete::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.Empty
      }
      shouldThrow<SerializationException> {
        reader.read(call)
      }
    }

  @Test
  fun `delete, malformed path param`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Delete::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "2eDS1sMt")
        }
      }
      shouldThrow<IllegalArgumentException> {
        reader.read(call)
      }.shouldHaveMessage("Malformed library book ID (value=2eDS1sMt).")
    }

  @Test
  fun `delete, happy path`(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Delete::class)
      val call = mockk<RoutingCall> {
        every { parameters } returns Parameters.build {
          append("libraryBookId", "library_book_2eDS1sMt")
        }
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Delete(
            libraryBookId = LibraryBookId("library_book_2eDS1sMt"),
          ),
        )
    }
}
