package kairo.rest.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import kairo.rest.endpoint.RestEndpoint
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenPathLibraryBookApi]
 * to test cases where the [RestEndpoint.Path] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenPathRestEndpointTemplateTest {
  @Test
  fun missingMethod(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.MissingPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.MissingPath" +
        " is missing @Path.",
    )
  }

  @Test
  fun emptyMethod(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.EmptyPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.EmptyPath" +
        " path must start with a slash: .",
    )
  }

  @Test
  fun pathMissingLeadingSlash(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.PathMissingLeadingSlash::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.PathMissingLeadingSlash" +
        " path must start with a slash: library-books.",
    )
  }

  @Test
  fun malformedConstantPathComponent(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.MalformedConstantPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.MalformedConstantPathComponent" +
        " path is invalid. Path constants must be kebab case: libraryBooks.",
    )
  }

  @Test
  fun malformedPathConstantComponent(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.MalformedConstantPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.MalformedConstantPathComponent" +
        " path is invalid. Path constants must be kebab case: libraryBooks.",
    )
  }

  @Test
  fun malformedParamPathComponent(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.MalformedParamPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.MalformedParamPathComponent" +
        " path is invalid. Path params must be camel case: library-book-id.",
    )
  }

  @Test
  fun nullablePathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.NullablePathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.NullablePathParam" +
        " path is invalid. Path param must not be nullable: libraryBookId.",
    )
  }

  @Test
  fun optionalPathParam(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.OptionalPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.OptionalPathParam" +
        " path is invalid. Path param must not be optional: libraryBookId.",
    )
  }

  @Test
  fun duplicatePathParamInPath(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.DuplicatePathParamInPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.DuplicatePathParamInPath" +
        " path is invalid. Duplicate path param in path: libraryBookId.",
    )
  }

  @Test
  fun pathParamInPathButNotInConstructor(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.PathParamInPathButNotInConstructor::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.PathParamInPathButNotInConstructor" +
        " path is invalid. Path param in path but not in constructor: libraryBookId.",
    )
  }

  @Test
  fun pathParamInConstructorButNotInPath(): Unit = runTest {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.from(BrokenPathLibraryBookApi.PathParamInConstructorButNotInPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.rest.template.BrokenPathLibraryBookApi.PathParamInConstructorButNotInPath" +
        " path is invalid. Path param in constructor but not in path: libraryBookId.",
    )
  }
}
