package kairo.adminSample.libraryBook

import kairo.rest.Rest
import kairo.rest.RestEndpoint

public object LibraryBookApi {
  @Rest("GET", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  public data class Get(
    @RestEndpoint.PathParam val libraryBookId: String,
  ) : RestEndpoint<Unit, LibraryBookRep>()

  @Rest("GET", "/library-books")
  @Rest.Accept("application/json")
  public data object ListAll : RestEndpoint<Unit, List<LibraryBookRep>>()

  @Rest("POST", "/library-books")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  public data class Create(
    override val body: LibraryBookRep.Creator,
  ) : RestEndpoint<LibraryBookRep.Creator, LibraryBookRep>()

  @Rest("PATCH", "/library-books/:libraryBookId")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  public data class Update(
    @RestEndpoint.PathParam val libraryBookId: String,
    override val body: LibraryBookRep.Update,
  ) : RestEndpoint<LibraryBookRep.Update, LibraryBookRep>()

  @Rest("DELETE", "/library-books/:libraryBookId")
  @Rest.Accept("application/json")
  public data class Delete(
    @RestEndpoint.PathParam val libraryBookId: String,
  ) : RestEndpoint<Unit, LibraryBookRep>()
}
