package kairo.rest.template

import kairo.id.Id
import kairo.rest.LibraryBookRep
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [BrokenPathRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Path] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenPathLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Accept("application/json")
  internal data object MissingPath : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("")
  @RestEndpoint.Accept("application/json")
  internal data object EmptyPath : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("library-books")
  @RestEndpoint.Accept("application/json")
  internal data object PathMissingLeadingSlash : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/libraryBooks/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class MalformedConstantPathComponent(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:library-book-id")
  @RestEndpoint.Accept("application/json")
  internal data class MalformedParamPathComponent(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class NullablePathParam(
    @PathParam val libraryBookId: Id?,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class OptionalPathParam(
    @PathParam val libraryBookId: Id = Id("library_book", "2eDS1sMt"),
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class DuplicatePathParamInPath(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data object PathParamInPathButNotInConstructor : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class PathParamInConstructorButNotInPath(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()
}
