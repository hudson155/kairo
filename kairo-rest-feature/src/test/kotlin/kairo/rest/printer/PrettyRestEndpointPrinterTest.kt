package kairo.rest.printer

import io.kotest.matchers.shouldBe
import kairo.rest.TypicalLibraryBookApi
import kairo.rest.template.RestEndpointTemplate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PrettyRestEndpointPrinterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Get::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books/:libraryBookId")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.ListAll::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByIsbn::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (isbn, strict?)")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByText::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (title?, author?)")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Create::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[application/json -> application/json] POST /library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Update::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[application/json -> application/json] PATCH /library-books/:libraryBookId")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Delete::class)
    PrettyRestEndpointPrinter.write(template)
      .shouldBe("[ -> application/json] DELETE /library-books/:libraryBookId")
  }
}
