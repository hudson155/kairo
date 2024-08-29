package kairo.rest

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PrettyRestEndpointWriterTest {
  @Test
  fun get(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Get::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET /library-books/:libraryBookId")
  }

  @Test
  fun listAll(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.ListAll::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET /library-books")
  }

  @Test
  fun searchByIsbn(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByIsbn::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (isbn)")
  }

  @Test
  fun searchByTitle(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.SearchByText::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] GET /library-books (title?, author?)")
  }

  @Test
  fun create(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Create::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[application/json -> application/json] POST /library-books")
  }

  @Test
  fun update(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Update::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[application/json -> application/json] PATCH /library-books/:libraryBookId")
  }

  @Test
  fun delete(): Unit = runTest {
    val template = RestEndpointTemplate.from(TypicalLibraryBookApi.Delete::class)
    PrettyRestEndpointWriter.write(template)
      .shouldBe("[ -> application/json] DELETE /library-books/:libraryBookId")
  }
}
