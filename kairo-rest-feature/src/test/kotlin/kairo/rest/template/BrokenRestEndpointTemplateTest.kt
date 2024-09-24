package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.template.BrokenLibraryBookApi as LibraryBookApi

/**
 * Tests simple invalid cases not related to a particular [RestEndpoint] annotation.
 */
internal class BrokenRestEndpointTemplateTest {
  @Test
  fun notDataClass(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.NotDataClass::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenLibraryBookApi.NotDataClass" +
        " must be a data class or data object.",
    )
  }

  @Test
  fun notDataObject(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.NotDataObject::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenLibraryBookApi.NotDataObject" +
        " must be a data class or data object.",
    )
  }
}
