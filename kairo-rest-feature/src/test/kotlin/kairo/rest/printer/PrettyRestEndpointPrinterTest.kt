package kairo.rest.printer

import io.kotest.matchers.shouldBe
import kairo.rest.template.RestEndpointTemplate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.TypicalLibraryBookApi as LibraryBookApi

internal class PrettyRestEndpointPrinterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.Get::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books/:libraryBookId")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.ListAll::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.SearchByIsbn::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (isbn)")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.SearchByText::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (title?, author?)")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.Create::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[application/json -> application/json] POST /library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.Update::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[application/json -> application/json] PATCH /library-books/:libraryBookId")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.from(LibraryBookApi.Delete::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] DELETE /library-books/:libraryBookId")
  }
}
