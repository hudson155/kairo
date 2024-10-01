package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
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
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.NonParamConstructorParameter::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.NonParamConstructorParameter" +
        " param must be path or query: shouldNotBeHere.",
    )
  }

  @Test
  fun bodyAsPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.BodyAsPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.BodyAsPathParam" +
        " body cannot be param.",
    )
  }

  @Test
  fun bodyAsQueryParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.BodyAsQueryParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.BodyAsQueryParam" +
        " body cannot be param.",
    )
  }

  @Test
  fun pathParamMarkedAsQueryParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.PathParamMarkedAsQueryParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.PathParamMarkedAsQueryParam" +
        " path is invalid. Path param in path but not in constructor: libraryBookId.",
    )
  }

  @Test
  fun queryParamMarkedAsPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.QueryParamMarkedAsPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.QueryParamMarkedAsPathParam" +
        " path is invalid. Path param in constructor but not in path: isbn.",
    )
  }

  @Test
  fun paramIsPathAndQuery(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.ParamIsPathAndQuery::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.ParamIsPathAndQuery" +
        " param cannot be both path and query: libraryBookId.",
    )
  }

  @Test
  fun nullablePathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.NullablePathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.NullablePathParam" +
        " path is invalid. Path param must not be nullable: libraryBookId.",
    )
  }

  @Test
  fun optionalPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.OptionalPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.OptionalPathParam" +
        " path is invalid. Path param must not be optional: libraryBookId.",
    )
  }

  @Test
  fun optionalQueryParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenParamLibraryBookApi.OptionalQueryParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenParamLibraryBookApi.OptionalQueryParam" +
        " query is invalid. Query param must not be optional: title.",
    )
  }
}
