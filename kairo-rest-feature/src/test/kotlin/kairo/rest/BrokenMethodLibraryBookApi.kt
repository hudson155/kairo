package kairo.rest

import kairo.id.KairoId

/**
 * This API is for [BrokenMethodRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Method] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenMethodLibraryBookApi {
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class MissingMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class EmptyMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("SYNC")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class UnsupportedMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()
}
