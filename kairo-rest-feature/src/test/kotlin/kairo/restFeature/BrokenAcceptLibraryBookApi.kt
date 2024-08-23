package kairo.restFeature

import kairo.id.KairoId

/**
 * This API is for [BrokenAcceptRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Accept] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenAcceptLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  internal data class AcceptNotPresentOnGet(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("application/json")
  internal data class AcceptNotPresentOnPost(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("")
  internal data class EmptyAccept(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application")
  internal data class MalformedAccept(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()
}
