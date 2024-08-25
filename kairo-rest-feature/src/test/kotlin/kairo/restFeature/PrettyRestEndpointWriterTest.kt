package kairo.restFeature

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PrettyRestEndpointWriterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Get::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET library/library-books/:libraryBookId")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.ListAll::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET library/library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByIsbn::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET library/library-books (isbn)")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.SearchByText::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET library/library-books (title?, author?)")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Create::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[application/json -> application/json] POST library/library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Update::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[application/json -> application/json] PATCH library/library-books/:libraryBookId")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.parse(TypicalLibraryBookApi.Delete::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] DELETE library/library-books/:libraryBookId")
  }
}
