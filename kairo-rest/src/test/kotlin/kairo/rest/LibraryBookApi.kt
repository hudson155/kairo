package kairo.rest

import kairo.id.Id

/**
 * This API is used by many tests within the REST Feature.
 * Verify all usages if making changes.
 */
internal object LibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Get(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data object ListAll : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class SearchByIsbn(
    @QueryParam val isbn: String,
  ) : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class SearchByText(
    @QueryParam val title: String?,
    @QueryParam val author: String?,
  ) : RestEndpoint<Unit, List<LibraryBookRep>>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class Create(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("PATCH")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class Update(
    @PathParam val libraryBookId: Id,
    override val body: LibraryBookRep.Update,
  ) : RestEndpoint<LibraryBookRep.Update, LibraryBookRep>()

  @RestEndpoint.Method("DELETE")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Delete(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Unit, LibraryBookRep>()
}
