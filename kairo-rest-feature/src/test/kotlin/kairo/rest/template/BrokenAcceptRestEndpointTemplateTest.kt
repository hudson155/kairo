package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.HttpMethod
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.template.BrokenAcceptLibraryBookApi as LibraryBookApi

/**
 * This test uses [LibraryBookApi]
 * to test cases where the [RestEndpoint.Accept] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenAcceptRestEndpointTemplateTest {
  @Test
  fun acceptPresentOnGet(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.AcceptNotPresentOnGet::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenAcceptLibraryBookApi.AcceptNotPresentOnGet" +
        " requires @Accept.",
    )
  }

  @Test
  fun acceptNotPresentOnPost(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.AcceptNotPresentOnPost::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenAcceptLibraryBookApi.AcceptNotPresentOnPost" +
        " requires @Accept.",
    )
  }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun emptyAccept(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.EmptyAccept::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = null,
        ),
      )
  }

  /**
   * This is actually valid; means "Any" content type.
   */
  @Test
  fun starAccept(): Unit = runTest {
    RestEndpointTemplate.from(LibraryBookApi.StarAccept::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Get,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library-books"),
            RestEndpointPath.Component.Param("libraryBookId"),
          ),
          query = RestEndpointQuery.of(),
          contentType = null,
          accept = null,
        ),
      )
  }

  @Test
  fun malformedAccept(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.MalformedAccept::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenAcceptLibraryBookApi.MalformedAccept" +
        " accept is invalid. Bad Content-Type format: application.",
    )
  }
}
