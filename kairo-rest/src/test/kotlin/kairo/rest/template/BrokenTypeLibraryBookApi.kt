package kairo.rest.template

import kairo.id.Id
import kairo.rest.LibraryBookRep
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [BrokenRestEndpointTemplateTest]
 * which tests invalid cases related to types within [RestEndpoint].
 */
internal object BrokenTypeLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class GetWithBodyNotProvided(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class GetWithBodyProvided(
    @PathParam val libraryBookId: Id,
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("PATCH")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class PatchWithoutBodyNotProvided(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("PATCH")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class PatchWithoutBodyProvided(
    @PathParam val libraryBookId: Id,
    override val body: Nothing,
  ) : RestEndpoint<Nothing, LibraryBookRep>()
}
