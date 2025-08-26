package kairo.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

internal object MethodVariantsLibraryBookApi {
  @Rest("SYNC", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class Sync(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest.Accept("application/json")
  internal data class Missing(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class Empty(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookId>()
}
