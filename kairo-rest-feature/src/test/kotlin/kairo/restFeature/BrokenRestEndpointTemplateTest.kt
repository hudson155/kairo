package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenLibraryBookApi]
 * to test simple invalid cases not related to a particular [RestEndpoint] annotation.
 */
internal class BrokenRestEndpointTemplateTest {
  @Test
  fun notDataClass(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenLibraryBookApi.NotDataClass::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenLibraryBookApi.NotDataClass" +
        " must be a data class or data object.",
    )
  }

  @Test
  fun notDataObject(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenLibraryBookApi.NotDataObject::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenLibraryBookApi.NotDataObject" +
        " must be a data class or data object.",
    )
  }
}
