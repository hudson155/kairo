package kairo.rest

import kairo.id.KairoId

/**
 * This API is for [BrokenParamRestEndpointTemplateTest]
 * which tests cases where the [RestEndpoint.PathParam] and [RestEndpoint.QueryParam] annotations
 * are used in unexpected or unsupported ways.
 */
internal object BrokenParamLibraryBookApi {
  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class NonParamConstructorParameter(
    val shouldNotBeHere: String,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books/:body")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class BodyAsPathParam(
    @PathParam override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("POST")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.ContentType("application/json")
  @RestEndpoint.Accept("application/json")
  internal data class BodyAsQueryParam(
    @QueryParam override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class PathParamMarkedAsQueryParam(
    @QueryParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class QueryParamMarkedAsPathParam(
    @PathParam val isbn: String,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class ParamIsPathAndQuery(
    @PathParam @QueryParam val libraryBookId: KairoId,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class NullablePathParam(
    @PathParam val libraryBookId: KairoId?,
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books/:libraryBookId")
  @RestEndpoint.Accept("application/json")
  internal data class OptionalPathParam(
    @PathParam val libraryBookId: KairoId = KairoId("library_book", "2eDS1sMt"),
  ) : RestEndpoint<Nothing, LibraryBookRep?>()

  @RestEndpoint.Method("GET")
  @RestEndpoint.Path("/library/library-books")
  @RestEndpoint.Accept("application/json")
  internal data class OptionalQueryParam(
    @QueryParam val title: String? = null,
    @QueryParam val author: String? = null,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()
}
