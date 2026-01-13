package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.libraryBook.BrokenLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrokenRestEndpointTemplateTest {
  @Test
  fun `not data class`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataClass::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataClass:" +
          " Must be a data class or data object.",
      )
    }

  @Test
  fun `not data object`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataObject::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataObject:" +
          " Must be a data class or data object.",
      )
    }

  @Test
  fun `non-param constructor parameter`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NonParamConstructorParameter::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NonParamConstructorParameter:" +
          " Param must be path or query (param=shouldNotBeHere).",
      )
    }

  @Test
  fun `body as path param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.BodyAsPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.BodyAsPathParam:" +
          " Body cannot be param.",
      )
    }

  @Test
  fun `body as query param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.BodyAsQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.BodyAsQueryParam:" +
          " Body cannot be param.",
      )
    }

  @Test
  fun `path param marked as query param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.PathParamMarkedAsQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.PathParamMarkedAsQueryParam:" +
          " Missing @RestEndpoint.PathParam (param=libraryBookId).",
      )
    }

  @Test
  fun `query param marked as path param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.QueryParamMarkedAsPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.QueryParamMarkedAsPathParam:" +
          " Unused @RestEndpoint.PathParam (param=isbn).",
      )
    }

  @Test
  fun `param is path and query`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.ParamIsPathAndQuery::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.ParamIsPathAndQuery:" +
          " Param cannot be both path and query (param=libraryBookId).",
      )
    }

  @Test
  fun `nullable path param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NullablePathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NullablePathParam:" +
          " @RestEndpoint.PathParam must not be nullable (param=libraryBookId).",
      )
    }

  @Test
  fun `optional path param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.OptionalPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.OptionalPathParam:" +
          " @RestEndpoint.PathParam must not be optional (param=libraryBookId).",
      )
    }

  @Test
  fun `nullable query param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NullableQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NullableQueryParam:" +
          " Nullable @RestEndpoint.QueryParam must be optional (param=title).",
      )
    }
}
