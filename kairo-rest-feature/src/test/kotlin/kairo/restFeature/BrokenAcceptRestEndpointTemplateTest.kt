package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenAcceptLibraryBookApi]
 * to test cases where the [RestEndpoint.Accept] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenAcceptRestEndpointTemplateTest {
  @Test
  fun acceptPresentOnGet() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenAcceptLibraryBookApi.AcceptNotPresentOnGet::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenAcceptLibraryBookApi.AcceptNotPresentOnGet" +
        " requires @Accept.",
    )
  }

  @Test
  fun acceptNotPresentOnPost() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenAcceptLibraryBookApi.AcceptNotPresentOnPost::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenAcceptLibraryBookApi.AcceptNotPresentOnPost" +
        " requires @Accept.",
    )
  }

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Test
  fun emptyAccept() {
    RestEndpointTemplate.parse(BrokenAcceptLibraryBookApi.EmptyAccept::class)
      .shouldBe(
        RestEndpointTemplate(
          method = HttpMethod.Post,
          path = RestEndpointPath.of(
            RestEndpointPath.Component.Constant("library"),
            RestEndpointPath.Component.Constant("library-books"),
          ),
          query = RestEndpointQuery.of(),
          contentType = ContentType.Application.Json,
          accept = ContentType.Any,
        ),
      )
  }

  @Test
  fun malformedAccept() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenAcceptLibraryBookApi.MalformedAccept::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenAcceptLibraryBookApi.MalformedAccept" +
        " accept is invalid. Bad Content-Type format: application.",
    )
  }
}
