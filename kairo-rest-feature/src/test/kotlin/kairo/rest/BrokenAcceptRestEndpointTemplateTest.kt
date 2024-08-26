package kairo.rest

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenAcceptLibraryBookApi]
 * to test cases where the [RestEndpoint.Accept] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenAcceptRestEndpointTemplateTest {
  @Test
  fun acceptPresentOnGet(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenAcceptLibraryBookApi.AcceptNotPresentOnGet::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenAcceptLibraryBookApi.AcceptNotPresentOnGet" +
        " requires @Accept.",
    )
  }

  @Test
  fun acceptNotPresentOnPost(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenAcceptLibraryBookApi.AcceptNotPresentOnPost::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenAcceptLibraryBookApi.AcceptNotPresentOnPost" +
        " requires @Accept.",
    )
  }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun emptyAccept(): Unit = runTest {
    RestEndpointTemplate.from(BrokenAcceptLibraryBookApi.EmptyAccept::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = ContentType.Any,
        ),
      )
  }

  @Test
  fun malformedAccept(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenAcceptLibraryBookApi.MalformedAccept::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.BrokenAcceptLibraryBookApi.MalformedAccept" +
        " accept is invalid. Bad Content-Type format: application.",
    )
  }
}
