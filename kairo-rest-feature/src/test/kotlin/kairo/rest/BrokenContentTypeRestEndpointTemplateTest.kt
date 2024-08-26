package kairo.rest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenContentTypeLibraryBookApi]
 * to test cases where the [RestEndpoint.ContentType] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenContentTypeRestEndpointTemplateTest {
  @Test
  fun contentTypePresentOnGet(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenContentTypeLibraryBookApi.ContentTypePresentOnGet::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenContentTypeLibraryBookApi.ContentTypePresentOnGet" +
        " may not have @ContentType since it does not have a body.",
    )
  }

  @Test
  fun contentTypeNotPresentOnPost(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenContentTypeLibraryBookApi.ContentTypeNotPresentOnPost::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenContentTypeLibraryBookApi.ContentTypeNotPresentOnPost" +
        " requires @ContentType since it has a body.",
    )
  }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun emptyContentType(): Unit = runTest {
    RestEndpointTemplate.from(BrokenContentTypeLibraryBookApi.EmptyContentType::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Post,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(),
          contentType = ContentType.Any,
          accept = ContentType.Application.Json,
        ),
      )
  }

  @Test
  fun malformedContentType(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenContentTypeLibraryBookApi.MalformedContentType::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenContentTypeLibraryBookApi.MalformedContentType" +
        " content type is invalid. Bad Content-Type format: application.",
    )
  }
}
