package kairo.rest.reader

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.libraryBook.BrokenLibraryBookApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

internal class BrokenRestEndpointReaderTest {
  private val json: Json = Json

  @Test
  fun notDataClass(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointReader.from(json, BrokenLibraryBookApi.NotDataClass::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi.NotDataClass::class.qualifiedName}:" +
          " Must be a data class or data object.",
      )
    }

  @Test
  fun notDataObject(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointReader.from(json, BrokenLibraryBookApi.NotDataObject::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi.NotDataObject::class.qualifiedName}:" +
          " Must be a data class or data object.",
      )
    }
}
