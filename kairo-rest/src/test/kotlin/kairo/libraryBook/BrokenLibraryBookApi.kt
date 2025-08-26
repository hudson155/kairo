package kairo.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

internal object BrokenLibraryBookApi {
  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal class NotDataClass(
    @PathParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  internal object NotDataObject : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  internal data class NonParamConstructorParameter(
    val shouldNotBeHere: String,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @Rest("POST", "/library-books/:body")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  internal data class BodyAsPathParam(
    @PathParam override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("POST", "/library-books")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  internal data class BodyAsQueryParam(
    @QueryParam override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class PathParamMarkedAsQueryParam(
    @QueryParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  internal data class QueryParamMarkedAsPathParam(
    @PathParam val isbn: String,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class ParamIsPathAndQuery(
    @PathParam @QueryParam val libraryBookId: LibraryBookId,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class NullablePathParam(
    @PathParam val libraryBookId: LibraryBookId?,
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  internal data class OptionalPathParam(
    @PathParam val libraryBookId: LibraryBookId = LibraryBookId("library_book_2eDS1sMt"),
  ) : RestEndpoint<Nothing, LibraryBookRep>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  internal data class OptionalQueryParam(
    @QueryParam val title: String? = null,
    @QueryParam val author: String? = null,
  ) : RestEndpoint<Nothing, List<LibraryBookRep>>()
}
