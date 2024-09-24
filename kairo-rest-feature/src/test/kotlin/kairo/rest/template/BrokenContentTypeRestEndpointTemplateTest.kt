package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.template.BrokenContentTypeLibraryBookApi as LibraryBookApi

/**
 * Tests cases where the [RestEndpoint.ContentType] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenContentTypeRestEndpointTemplateTest {
  @Test
  fun contentTypePresentOnGet(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.ContentTypePresentOnGet::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenContentTypeLibraryBookApi.ContentTypePresentOnGet" +
        " may not have @ContentType since it does not have a body.",
    )
  }

  @Test
  fun contentTypeNotPresentOnPost(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.ContentTypeNotPresentOnPost::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenContentTypeLibraryBookApi.ContentTypeNotPresentOnPost" +
        " requires @ContentType since it has a body.",
    )
  }

  /**
   * An empty string means "Any" content type, which is invalid.
   */
  @Test
  fun emptyContentType(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.EmptyContentType::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenContentTypeLibraryBookApi.EmptyContentType" +
        " content type cannot be */*.",
    )
  }

  /**
   * Means "Any" content type, which is invalid.
   */
  @Test
  fun starContentType(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.StarContentType::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenContentTypeLibraryBookApi.StarContentType" +
        " content type cannot be */*.",
    )
  }

  @Test
  fun malformedContentType(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.MalformedContentType::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenContentTypeLibraryBookApi.MalformedContentType" +
        " content type is invalid. Bad Content-Type format: application.",
    )
  }
}
