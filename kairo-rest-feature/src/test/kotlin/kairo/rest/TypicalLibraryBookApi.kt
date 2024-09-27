package kairo.rest

import kairo.id.KairoId
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is used by many tests within the REST Feature.
 * Verify all usages if making changes.
 */
internal object TypicalLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Get(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data object ListAll : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class SearchByIsbn(
    @QueryParam val isbn: String,
    @QueryParam val strict: Boolean?,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class SearchByText(
    @QueryParam val title: String?,
    @QueryParam val author: String?,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

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
    @PathParam val libraryBookId: KairoId,
    override val body: LibraryBookRep.Update,
  ) : RestEndpoint<LibraryBookRep.Update, LibraryBookRep>()

  @RestEndpoint.Method("DELETE")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class Delete(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()
}
