package kairo.rest.reader

import io.kotest.matchers.shouldBe
import io.ktor.http.Parameters
import io.ktor.server.routing.RoutingCall
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kairo.libraryBook.LibraryBookApi
import kairo.libraryBook.LibraryBookId
import kairo.libraryBook.LibraryBookRep
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RestEndpointReaderTest {
  @Test
  fun get(): Unit =
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
  fun listAll(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.ListAll::class)
      val call = mockk<RoutingCall>()
      reader.read(call)
        .shouldBe(
          LibraryBookApi.ListAll,
        )
    }

  @Test
  fun searchByIsbn(): Unit =
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
  fun `searchByText (none provided)`(): Unit =
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
  fun `searchByText, (all provided)`(): Unit =
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

  @Test
  fun create(): Unit =
    runTest {
      val reader = RestEndpointReader.from(LibraryBookApi.Create::class)
      val call = mockk<RoutingCall> {
        coEvery {
          receiveNullable<LibraryBookRep.Creator>(any())
        } returns LibraryBookRep.Creator(
          title = "Mere Christianity",
          authors = listOf("C. S. Lewis"),
          isbn = "978-0060652920",
        )
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Create(
            body = LibraryBookRep.Creator(
              title = "Mere Christianity",
              authors = listOf("C. S. Lewis"),
              isbn = "978-0060652920",
            ),
          ),
        )
    }

  @Test
  fun `update, (none provided)`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, (title null)`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, (authors empty)`(): Unit =
    runTest {
      // TODO: Test partial updates.
    }

  @Test
  fun `update, (all provided)`(): Unit =
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
        )
      }
      reader.read(call)
        .shouldBe(
          LibraryBookApi.Update(
            libraryBookId = LibraryBookId("library_book_2eDS1sMt"),
            body = LibraryBookRep.Update(
              title = "The Meaning of Marriage: Facing the Complexities of Commitment with the Wisdom of God",
              authors = listOf("Timothy Keller", "Kathy Keller"),
            ),
          ),
        )
    }

  @Test
  fun delete(): Unit =
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
