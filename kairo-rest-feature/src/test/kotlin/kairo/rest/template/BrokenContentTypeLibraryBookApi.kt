package kairo.rest.template

import kairo.id.KairoId
import kairo.rest.TypicalLibraryBookRep
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [BrokenContentTypeRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.ContentType] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenContentTypeLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class ContentTypePresentOnGet(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, TypicalLibraryBookRep?>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class ContentTypeNotPresentOnPost(
    override val body: TypicalLibraryBookRep.Creator,
  ) : RestEndpoint<TypicalLibraryBookRep.Creator, TypicalLibraryBookRep>()

  /**
   * An empty string means "Any" content type, which is invalid.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("")
  @RestEndpoint.Accept("application/json")
  internal data class EmptyContentType(
    override val body: TypicalLibraryBookRep.Creator,
  ) : RestEndpoint<TypicalLibraryBookRep.Creator, TypicalLibraryBookRep>()

  /**
   * Means "Any" content type, which is invalid.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("")
  @RestEndpoint.Accept("application/json")
  internal data class StarContentType(
    override val body: TypicalLibraryBookRep.Creator,
  ) : RestEndpoint<TypicalLibraryBookRep.Creator, TypicalLibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application")
  @RestEndpoint.Accept("application/json")
  internal data class MalformedContentType(
    override val body: TypicalLibraryBookRep.Creator,
  ) : RestEndpoint<TypicalLibraryBookRep.Creator, TypicalLibraryBookRep>()
}
