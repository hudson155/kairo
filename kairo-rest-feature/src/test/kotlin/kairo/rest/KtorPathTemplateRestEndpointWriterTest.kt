package kairo.rest

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class KtorPathTemplateRestEndpointWriterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Get::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.ListAll::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByIsbn::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByText::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Create::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Update::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Delete::class)
    KtorPathTemplateRestEndpointWriter.write(template)
      .shouldBe("library/library-books/{libraryBookId}")
  }
}
