package limber.api.organization

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.QueryParam
import limber.feature.rest.RestEndpoint
import limber.rep.organization.OrganizationRep
import limber.validation.SearchValidator

public object OrganizationApi {
  public data class Get(
    val organizationId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationId"
  }

  public data object ListAll : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations"
  }

  public data class Search(
    @SearchValidator val search: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations"
    override val qp: List<QueryParam> = listOf(::search)
  }

  public data class Create(
    @Valid override val body: OrganizationRep.Creator?,
  ) : RestEndpoint<OrganizationRep.Creator>() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations"
  }

  public data class Update(
    val organizationId: String,
    @Valid override val body: OrganizationRep.Update?,
  ) : RestEndpoint<OrganizationRep.Update>() {
    override val method: HttpMethod = HttpMethod.Patch
    override val path: String = "/organizations/$organizationId"
  }
}
