package kairo.rest.template

import kairo.id.KairoId
import kairo.rest.TypicalLibraryBookRep
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [BrokenMethodRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.Method] annotation
 * is used in unexpected or unsupported ways.
 */
internal object BrokenMethodLibraryBookApi {
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class MissingMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, TypicalLibraryBookRep?>()

  @RestEndpoint.Method("")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class EmptyMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, TypicalLibraryBookRep?>()

  @RestEndpoint.Method("SYNC")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class UnsupportedMethod(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, TypicalLibraryBookRep?>()
}
