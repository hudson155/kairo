package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.libraryBook.BrokenLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class BrokenRestEndpointTemplateTest {
  @Test
  fun notDataClass(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataClass::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataClass:" +
          " Must be a data class or data object.",
      )
    }

  @Test
  fun notDataObject(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NotDataObject::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NotDataObject:" +
          " Must be a data class or data object.",
      )
    }

  @Test
  fun nonParamConstructorParameter(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NonParamConstructorParameter::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NonParamConstructorParameter:" +
          " Param must be path or query (param=shouldNotBeHere).",
      )
    }

  @Test
  fun bodyAsPathParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.BodyAsPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.BodyAsPathParam:" +
          " Body cannot be param.",
      )
    }

  @Test
  fun bodyAsQueryParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.BodyAsQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.BodyAsQueryParam:" +
          " Body cannot be param.",
      )
    }

  @Test
  fun pathParamMarkedAsQueryParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.PathParamMarkedAsQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.PathParamMarkedAsQueryParam:" +
          " @RestEndpoint.Path is invalid. Missing @RestEndpoint.PathParam (param=libraryBookId).",
      )
    }

  @Test
  fun queryParamMarkedAsPathParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.QueryParamMarkedAsPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.QueryParamMarkedAsPathParam:" +
          " @RestEndpoint.Path is invalid. Missing @RestEndpoint.PathParam (param=isbn).",
      )
    }

  @Test
  fun paramIsPathAndQuery(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.ParamIsPathAndQuery::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.ParamIsPathAndQuery:" +
          " Param cannot be both path and query (param=libraryBookId).",
      )
    }

  @Test
  fun nullablePathParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.NullablePathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.NullablePathParam:" +
          " @RestEndpoint.PathParam must not be nullable (param=libraryBookId).",
      )
    }

  @Test
  fun optionalPathParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.OptionalPathParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.OptionalPathParam:" +
          " @RestEndpoint.PathParam must not be optional (param=libraryBookId).",
      )
    }

  @Test
  fun optionalQueryParam(): Unit =
    runTest {
      shouldThrow<IllegalArgumentException> {
        RestEndpointTemplate.from(BrokenLibraryBookApi.OptionalQueryParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${BrokenLibraryBookApi::class.qualifiedName}.OptionalQueryParam:" +
          " @RestEndpoint.QueryParam must not be optional (param=title).",
      )
    }
}
