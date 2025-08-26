package kairo.libraryBook

import kairo.rest.RestEndpoint

internal object ContentTypeVariantsLibraryBookApi {
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("text/csv")
  @RestEndpoint.Accept("application/json")
  internal data class Csv(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class PresentOnGet(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class NotPresentOnPost(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("")
  @RestEndpoint.Accept("application/json")
  internal data class Empty(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * Means "Any" content type.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("*/*")
  @RestEndpoint.Accept("application/json")
  internal data class Star(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application")
  @RestEndpoint.Accept("application/json")
  internal data class Malformed(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
