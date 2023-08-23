package limber.api.organizationHostname

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.organizationHostname.OrganizationHostnameRep

public object OrganizationHostnameApi {
  public data class Get(
    val hostnameId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/hostnames/$hostnameId"
  }

  public data class ListByOrganization(
    val organizationId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationId/hostnames"
  }

  public data class Create(
    val organizationId: String,
    @Valid override val body: OrganizationHostnameRep.Creator?,
  ) : RestEndpoint<OrganizationHostnameRep.Creator>() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationId/hostnames"
  }

  public data class Delete(
    val hostnameId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/hostnames/$hostnameId"
  }
}
