package kairo.restFeature

import kairo.id.KairoId

/**
 * This API is for [BrokenRestEndpointTemplateTest]
 * which tests invalid cases related to types within [RestEndpoint].
 */
internal object BrokenTypeLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class GetWithBodyNotProvided(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class GetWithBodyProvided(
    @PathParam val libraryBookId: KairoId,
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep?>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class PostWithoutBodyNotProvided(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class PostWithoutBodyProvided(
    @PathParam val libraryBookId: KairoId,
    override val body: Nothing,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()
}
