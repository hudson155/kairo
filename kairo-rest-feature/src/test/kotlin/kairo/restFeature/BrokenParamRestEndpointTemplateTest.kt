package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenParamLibraryBookApi]
 * to test cases where the [RestEndpoint.PathParam] and [RestEndpoint.QueryParam] annotations
 * are used in unexpected or unsupported ways.
 */
internal class BrokenParamRestEndpointTemplateTest {
  @Test
  fun nonParamConstructorParameter(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.NonParamConstructorParameter::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.NonParamConstructorParameter" +
        " param must be path or query: shouldNotBeHere.",
    )
  }

  @Test
  fun bodyAsPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.BodyAsPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.BodyAsPathParam" +
        " body cannot be param.",
    )
  }

  @Test
  fun bodyAsQueryParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.BodyAsQueryParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.BodyAsQueryParam" +
        " body cannot be param.",
    )
  }

  @Test
  fun pathParamMarkedAsQueryParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.PathParamMarkedAsQueryParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.PathParamMarkedAsQueryParam" +
        " path is invalid. Path param in path but not in constructor: libraryBookId.",
    )
  }

  @Test
  fun queryParamMarkedAsPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.QueryParamMarkedAsPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.QueryParamMarkedAsPathParam" +
        " path is invalid. Path param in constructor but not in path: isbn.",
    )
  }

  @Test
  fun paramIsPathAndQuery(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenParamLibraryBookApi.ParamIsPathAndQuery::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenParamLibraryBookApi.ParamIsPathAndQuery" +
        " param cannot be both path and query: libraryBookId.",
    )
  }
}
