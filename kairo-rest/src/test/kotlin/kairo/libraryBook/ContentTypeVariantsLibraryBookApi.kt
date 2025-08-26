package kairo.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

internal object ContentTypeVariantsLibraryBookApi {
  @Rest("POST", "/library-books")
  @Rest.ContentType("text/csv")
  @Rest.Accept("application/json")
  internal data class Csv(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  internal data class PresentOnGet(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("POST", "/library-books")
  @Rest.Accept("application/json")
  internal data class NotPresentOnPost(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @Rest("POST", "/library-books")
  @Rest.ContentType("")
  @Rest.Accept("application/json")
  internal data class Empty(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * Means "Any" content type.
   */
  @Rest("POST", "/library-books")
  @Rest.ContentType("*/*")
  @Rest.Accept("application/json")
  internal data class Star(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("POST", "/library-books")
  @Rest.ContentType("application")
  @Rest.Accept("application/json")
  internal data class Malformed(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
