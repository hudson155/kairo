package kairo.rest.printer

import io.kotest.matchers.shouldBe
import kairo.rest.TypicalLibraryBookApi
import kairo.rest.template.RestEndpointTemplate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KtorPathTemplateRestEndpointPrinterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Get::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books/{libraryBookId}")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.ListAll::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByIsbn::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByText::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Create::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Update::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books/{libraryBookId}")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Delete::class)
    KtorPathTemplateRestEndpointPrinter.write(template)
      .shouldBe("/library-books/{libraryBookId}")
  }
}
