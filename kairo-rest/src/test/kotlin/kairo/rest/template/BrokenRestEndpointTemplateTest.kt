package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.libraryBook.BrokenLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrokenRestEndpointTemplateTest {
  @Test
  fun notDataClass(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataClass::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataClass" +
          " must be a data class or data object.",
      )
    }

  @Test
  fun notDataObject(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataObject::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataObject" +
          " must be a data class or data object.",
      )
    }
}
