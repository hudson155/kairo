package kairo.rest.template

import io.kotest.matchers.shouldBe
import kairo.libraryBook.LibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class RestEndpointTemplateToKtorPathTest {
  @Test
  fun get(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Get::class).toKtorPath()
        .shouldBe("/library-books/{libraryBookId}")
    }

  @Test
  fun listByIds(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.ListByIds::class).toKtorPath()
        .shouldBe("/library-books")
    }

  @Test
  fun listAll(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.ListAll::class).toKtorPath()
        .shouldBe("/library-books")
    }

  @Test
  fun searchByIsbn(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByIsbn::class).toKtorPath()
        .shouldBe("/library-books")
    }

  @Test
  fun searchByTitle(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.SearchByText::class).toKtorPath()
        .shouldBe("/library-books")
    }

  @Test
  fun create(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Create::class).toKtorPath()
        .shouldBe("/library-books")
    }

  @Test
  fun update(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Update::class).toKtorPath()
        .shouldBe("/library-books/{libraryBookId}")
    }

  @Test
  fun delete(): Unit =
    runTest {
      RestEndpointTemplate.from(LibraryBookApi.Delete::class).toKtorPath()
        .shouldBe("/library-books/{libraryBookId}")
    }
}
