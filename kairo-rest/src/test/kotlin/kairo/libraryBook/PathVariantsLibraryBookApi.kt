package kairo.libraryBook

import kairo.rest.RestEndpoint

internal object PathVariantsLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Accept("application/json")
  internal data object Missing : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("")
  @RestEndpoint.Accept("application/json")
  internal data object Empty : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/")
  @RestEndpoint.Accept("application/json")
  internal data object Root : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("library-books")
  @RestEndpoint.Accept("application/json")
  internal data object MissingLeadingSlash : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class NullableParam(
    @PathParam val libraryBookId: LibraryBookId?,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class OptionalParam(
    @PathParam val libraryBookId: LibraryBookId = LibraryBookId("library_book_2eDS1sMt"),
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class DuplicateParam(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data object ParamNotInConstructor : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class ParamNotInPath(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()
}
