package kairo.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

internal object PathVariantsLibraryBookApi {
  @Rest.Accept("application/json")
  internal data object Missing : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("GET", "")
  @Rest.Accept("application/json")
  internal data object Empty : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("GET", "/")
  @Rest.Accept("application/json")
  internal data object Root : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("GET", "library-books")
  @Rest.Accept("application/json")
  internal data object MissingLeadingSlash : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class NullableParam(
    @PathParam val libraryBookId: LibraryBookId?,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class OptionalParam(
    @PathParam val libraryBookId: LibraryBookId = LibraryBookId("library_book_2eDS1sMt"),
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class DuplicateParam(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data object ParamNotInConstructor : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  internal data class ParamNotInPath(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()
}
