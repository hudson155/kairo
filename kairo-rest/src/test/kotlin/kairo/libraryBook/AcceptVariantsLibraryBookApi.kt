package kairo.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

internal object AcceptVariantsLibraryBookApi {
  @Rest("POST", "/library-books")
  @Rest.ContentType("application/json")
  @Rest.Accept("text/csv")
  internal data class Csv(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  internal data class NotPresentOnGet(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("POST", "/library-books")
  @Rest.ContentType("application/json")
  internal data class NotPresentOnPost(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Rest("POST", "/library-books")
  @Rest.Accept("")
  internal data class Empty(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * Means "Any" content type.
   */
  @Rest("POST", "/library-books")
  @Rest.Accept("*/*")
  internal data class Star(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("POST", "/library-books")
  @Rest.Accept("application")
  internal data class Malformed(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
