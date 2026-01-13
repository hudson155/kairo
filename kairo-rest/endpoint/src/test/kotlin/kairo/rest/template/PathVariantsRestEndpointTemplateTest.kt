package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import kairo.libraryBook.PathVariantsLibraryBookApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

internal class PathVariantsRestEndpointTemplateTest {
  @Test
  fun missing(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.Missing::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.Missing::class.qualifiedName}:" +
          " Must define @Rest.",
      )
    }

  @Test
  fun empty(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.Empty::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.Empty::class.qualifiedName}:" +
          " @Rest path is invalid. Must start with a slash.",
      )
    }

  @Test
  fun root(): Unit =
    runTest {
      RestEndpointTemplate.from(PathVariantsLibraryBookApi.Root::class)
        .shouldBe(
          RestEndpointTemplate(
            method = HttpMethod.Get,
            path = RestEndpointTemplatePath(),
            query = RestEndpointTemplateQuery(),
            contentType = null,
            accept = ContentType.Application.Json,
          ),
        )
    }

  @Test
  fun `missing leading slash`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.MissingLeadingSlash::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.MissingLeadingSlash::class.qualifiedName}:" +
          " @Rest path is invalid. Must start with a slash.",
      )
    }

  @Test
  fun `nullable param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.NullableParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.NullableParam::class.qualifiedName}:" +
          " @RestEndpoint.PathParam must not be nullable (param=libraryBookId).",
      )
    }

  @Test
  fun `optional param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.OptionalParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.OptionalParam::class.qualifiedName}:" +
          " @RestEndpoint.PathParam must not be optional (param=libraryBookId).",
      )
    }

  @Test
  fun `duplicate param`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.DuplicateParam::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.DuplicateParam::class.qualifiedName}:" +
          " @Rest path is invalid. Duplicate param (param=libraryBookId).",
      )
    }

  @Test
  fun `param not in constructor`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.ParamNotInConstructor::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.ParamNotInConstructor::class.qualifiedName}:" +
          " Missing @RestEndpoint.PathParam (param=libraryBookId).",
      )
    }

  @Test
  fun `param not in path`(): Unit =
    runTest {
      shouldThrowExactly<IllegalArgumentException> {
        RestEndpointTemplate.from(PathVariantsLibraryBookApi.ParamNotInPath::class)
      }.shouldHaveMessage(
        "REST endpoint ${PathVariantsLibraryBookApi.ParamNotInPath::class.qualifiedName}:" +
          " Unused @RestEndpoint.PathParam (param=libraryBookId).",
      )
    }
}
