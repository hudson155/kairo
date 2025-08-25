package kairo.libraryBook

import kairo.rest.RestEndpoint

internal object BrokenLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal class NotDataClass(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal object NotDataObject : RestEndpoint<Unit, List<LibraryBookRep>>()
}
