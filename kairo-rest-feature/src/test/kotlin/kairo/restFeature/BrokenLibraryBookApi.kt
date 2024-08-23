package kairo.restFeature

import kairo.id.KairoId

/**
 * This API is for [BrokenRestEndpointTemplateTest]
 * which tests simple invalid cases not related to a particular [RestEndpoint] annotation.
 */
internal object BrokenLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal class NotDataClass(
    @PathParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal object NotDataObject : RestEndpoint<Nothing, LibraryBookRep?>()
}
