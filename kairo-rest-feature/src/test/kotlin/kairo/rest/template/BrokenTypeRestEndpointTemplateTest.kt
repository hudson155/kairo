package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenLibraryBookApi]
 * to test invalid cases related to types within [RestEndpoint].
 */
internal class BrokenTypeRestEndpointTemplateTest {
  @Test
  fun getWithBodyNotProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.GetWithBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.GetWithBodyNotProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun getWithBodyProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.GetWithBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.GetWithBodyProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun patchWithoutBodyNotProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.PatchWithoutBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PatchWithoutBodyNotProvided" +
        " has method PATCH but does not specify a body.",
    )
  }

  @Test
  fun patchWithoutBodyProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.PatchWithoutBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PatchWithoutBodyProvided" +
        " has method PATCH but does not specify a body.",
    )
  }
}
