package kairo.restFeature

import kairo.id.KairoId

/**
 * This API is for [BrokenContentTypeRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.ContentType] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenContentTypeLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class ContentTypePresentOnGet(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class ContentTypeNotPresentOnPost(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  /**
   * This is actually valid; an empty string means "Any" content type.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("")
  @RestEndpoint.Accept("application/json")
  internal data class EmptyContentType(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("application")
  @RestEndpoint.Accept("application/json")
  internal data class MalformedContentType(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()
}
