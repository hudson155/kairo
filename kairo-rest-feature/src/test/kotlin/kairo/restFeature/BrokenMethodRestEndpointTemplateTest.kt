package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenMethodLibraryBookApi]
 * to test cases where the [RestEndpoint.Method] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenMethodRestEndpointTemplateTest {
  @Test
  fun missingMethod() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenMethodLibraryBookApi.MissingMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenMethodLibraryBookApi.MissingMethod" +
        " is missing @Method.",
    )
  }

  @Test
  fun emptyMethod() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenMethodLibraryBookApi.EmptyMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenMethodLibraryBookApi.EmptyMethod" +
        " has invalid method: .",
    )
  }

  @Test
  fun sync() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenMethodLibraryBookApi.UnsupportedMethod::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenMethodLibraryBookApi.UnsupportedMethod" +
        " has invalid method: SYNC.",
    )
  }
}
