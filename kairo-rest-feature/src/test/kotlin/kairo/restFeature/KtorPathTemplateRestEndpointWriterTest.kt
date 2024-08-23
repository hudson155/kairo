package kairo.restFeature

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class KtorPathTemplateRestEndpointWriterTest {
  @Test
  fun get() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Get::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }

  @Test
  fun listAll() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.ListAll::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun searchByIsbn() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByIsbn::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun searchByTitle() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByText::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun create() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Create::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun update() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Update::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }

  @Test
  fun delete() {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Delete::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }
}
