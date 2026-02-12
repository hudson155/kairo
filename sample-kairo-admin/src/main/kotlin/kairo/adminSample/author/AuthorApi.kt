package kairo.adminSample.author

import kairo.rest.Rest
import kairo.rest.RestEndpoint

public object AuthorApi {
  @Rest("GET", "/authors/:authorId")
  @Rest.Accept("application/json")
  public data class Get(
    @RestEndpoint.PathParam val authorId: String,
  ) : RestEndpoint<Unit, AuthorRep>()

  @Rest("GET", "/authors")
  @Rest.Accept("application/json")
  public data object ListAll : RestEndpoint<Unit, List<AuthorRep>>()

  @Rest("POST", "/authors")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  public data class Create(
    override val body: AuthorRep.Creator,
  ) : RestEndpoint<AuthorRep.Creator, AuthorRep>()

  @Rest("PATCH", "/authors/:authorId")
  @Rest.ContentType("application/json")
  @Rest.Accept("application/json")
  public data class Update(
    @RestEndpoint.PathParam val authorId: String,
    override val body: AuthorRep.Update,
  ) : RestEndpoint<AuthorRep.Update, AuthorRep>()

  @Rest("DELETE", "/authors/:authorId")
  @Rest.Accept("application/json")
  public data class Delete(
    @RestEndpoint.PathParam val authorId: String,
  ) : RestEndpoint<Unit, AuthorRep>()
}
