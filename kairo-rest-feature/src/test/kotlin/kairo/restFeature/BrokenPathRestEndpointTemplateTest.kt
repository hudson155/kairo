package kairo.restFeature

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

/**
 * This test uses [BrokenPathLibraryBookApi]
 * to test cases where the [RestEndpoint.Path] annotation
 * is used in unexpected or unsupported ways.
 */
internal class BrokenPathRestEndpointTemplateTest {
  @Test
  fun missingMethod() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.MissingPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.MissingPath" +
        " is missing @Path.",
    )
  }

  @Test
  fun emptyMethod() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.EmptyPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.EmptyPath" +
        " path must start with a slash: .",
    )
  }

  @Test
  fun pathMissingLeadingSlash() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.PathMissingLeadingSlash::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.PathMissingLeadingSlash" +
        " path must start with a slash: library/library-books.",
    )
  }

  @Test
  fun malformedConstantPathComponent() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.MalformedConstantPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.MalformedConstantPathComponent" +
        " path is invalid. Path constants must be kebab case: libraryBooks.",
    )
  }

  @Test
  fun malformedPathConstantComponent() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.MalformedConstantPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.MalformedConstantPathComponent" +
        " path is invalid. Path constants must be kebab case: libraryBooks.",
    )
  }

  @Test
  fun malformedParamPathComponent() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.MalformedParamPathComponent::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.MalformedParamPathComponent" +
        " path is invalid. Path params must be camel case: library-book-id.",
    )
  }

  @Test
  fun nullablePathParam() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.NullablePathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.NullablePathParam" +
        " path is invalid. Path param must not be nullable: libraryBookId.",
    )
  }

  @Test
  fun optionalPathParam() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.OptionalPathParam::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.OptionalPathParam" +
        " path is invalid. Path param must not be optional: libraryBookId.",
    )
  }

  @Test
  fun duplicatePathParamInPath() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.DuplicatePathParamInPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.DuplicatePathParamInPath" +
        " path is invalid. Duplicate path param in path: libraryBookId.",
    )
  }

  @Test
  fun pathParamInPathButNotInConstructor() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.PathParamInPathButNotInConstructor::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.PathParamInPathButNotInConstructor" +
        " path is invalid. Path param in path but not in constructor: libraryBookId.",
    )
  }

  @Test
  fun pathParamInConstructorButNotInPath() {
    shouldThrow<IllegalArgumentException> {
      RestEndpointTemplate.parse(BrokenPathLibraryBookApi.PathParamInConstructorButNotInPath::class)
    }.shouldHaveMessage(
      "REST endpoint kairo.restFeature.BrokenPathLibraryBookApi.PathParamInConstructorButNotInPath" +
        " path is invalid. Path param in constructor but not in path: libraryBookId.",
    )
  }
}
