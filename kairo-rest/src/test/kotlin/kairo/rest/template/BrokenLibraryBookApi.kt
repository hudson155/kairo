package kairo.rest.template

import kairo.id.Id
import kairo.rest.LibraryBookRep
import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [BrokenRestEndpointTemplateTest]
 * which tests simple invalid cases not related to a particular [RestEndpoint] annotation.
 */
internal object BrokenLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal class NotDataClass(
    @PathParam val libraryBookId: Id,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.Accept("application/json")
  internal object NotDataObject : RestEndpoint<Nothing, List<LibraryBookRep>>()
}
