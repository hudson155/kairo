package kairo.restFeature

import kairo.id.KairoId

/**
 * This API is for [BrokenPathRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Path] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenPathLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Accept("application/json")
  internal data object MissingPath : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("")
  @RestEndpoint.Accept("application/json")
  internal data object EmptyPath : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data object PathMissingLeadingSlash : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/libraryBooks/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class InvalidConstantPathComponent(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:library-book-id")
  @RestEndpoint.Accept("application/json")
  internal data class InvalidParamPathComponent(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class NullablePathParam(
    @PathParam val libraryBookId: KairoId?,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class OptionalPathParam(
    @PathParam val libraryBookId: KairoId = KairoId("library_book", "2eDS1sMt"),
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class DuplicatePathParamInPath(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data object PathParamInPathButNotInConstructor : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class PathParamInConstructorButNotInPath(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
