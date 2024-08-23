package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenLibraryBookApi]
 * to test invalid cases related to types within [RestEndpoint].
 */
internal class BrokenTypeRestEndpointTemplateTest {
  @Test
  fun getWithBodyNotProvided() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenTypeLibraryBookApi.GetWithBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenTypeLibraryBookApi.GetWithBodyNotProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun getWithBodyProvided() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenTypeLibraryBookApi.GetWithBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenTypeLibraryBookApi.GetWithBodyProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun postWithoutBodyNotProvided() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenTypeLibraryBookApi.PostWithoutBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenTypeLibraryBookApi.PostWithoutBodyProvided" +
        " has method POST but specifies a body.",
    )
  }

  @Test
  fun postWithoutBodyProvided() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenTypeLibraryBookApi.PostWithoutBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenTypeLibraryBookApi.PostWithoutBodyNotProvided" +
        " has method POST but specifies a body.",
    )
  }
}
