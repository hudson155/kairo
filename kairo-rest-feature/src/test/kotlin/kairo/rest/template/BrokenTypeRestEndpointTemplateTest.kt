package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
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
  fun postWithoutBodyNotProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.PostWithoutBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PostWithoutBodyProvided" +
        " has method POST but specifies a body.",
    )
  }

  @Test
  fun postWithoutBodyProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenTypeLibraryBookApi.PostWithoutBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PostWithoutBodyNotProvided" +
        " has method POST but specifies a body.",
    )
  }
}
