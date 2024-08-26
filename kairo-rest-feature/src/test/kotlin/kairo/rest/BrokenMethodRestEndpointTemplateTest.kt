package kairo.rest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenMethodLibraryBookApi]
 * to test cases where the [RestEndpoint.Method] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenMethodRestEndpointTemplateTest {
  @Test
  fun missingMethod(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenMethodLibraryBookApi.MissingMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenMethodLibraryBookApi.MissingMethod" +
        " is missing @Method.",
    )
  }

  @Test
  fun emptyMethod(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenMethodLibraryBookApi.EmptyMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenMethodLibraryBookApi.EmptyMethod" +
        " has invalid method: .",
    )
  }

  @Test
  fun sync(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenMethodLibraryBookApi.UnsupportedMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenMethodLibraryBookApi.UnsupportedMethod" +
        " has invalid method: SYNC.",
    )
  }
}
