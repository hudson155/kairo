package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kairo.rest.template.BrokenTypeLibraryBookApi as LibraryBookApi

/**
 * Tests invalid cases related to types within [RestEndpoint].
 */
internal class BrokenTypeRestEndpointTemplateTest {
  @Test
  fun getWithBodyNotProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.GetWithBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.GetWithBodyNotProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun getWithBodyProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.GetWithBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.GetWithBodyProvided" +
        " has method GET but specifies a body.",
    )
  }

  @Test
  fun postWithoutBodyNotProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.PostWithoutBodyProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PostWithoutBodyProvided" +
        " has method POST but specifies a body.",
    )
  }

  @Test
  fun postWithoutBodyProvided(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(LibraryBookApi.PostWithoutBodyNotProvided::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenTypeLibraryBookApi.PostWithoutBodyNotProvided" +
        " has method POST but specifies a body.",
    )
  }
}
