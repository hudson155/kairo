package kairo.rest.exceptionHandler

import kairo.rest.endpoint.RestEndpoint

/**
 * This API is for [ExceptionHandlerTest].
 * See that class for more information.
 */
internal object ExceptionHandlerLibraryBookApi {
  /**
   * This is not a realistic endpoint from a design perspective;
   * it aims to use all functionality that needs testing.
   */
  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class Create(
    @QueryParam val title: String,
    @QueryParam val isSeries: Boolean,
    override val body: ExceptionHandlerLibraryBookRep.Creator,
  ) : RestEndpoint<ExceptionHandlerLibraryBookRep.Creator, ExceptionHandlerLibraryBookRep>()
}
