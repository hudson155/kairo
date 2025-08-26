package kairo.libraryBook

import kairo.rest.RestEndpoint

internal object MethodVariantsLibraryBookApi {
  @RestEndpoint.Method("SYNC")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Sync(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Missing(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Empty(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookId>()
}
