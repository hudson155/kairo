package limber.api

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.rep.OrganizationHostnameRep
import limber.rest.RestEndpoint
import java.util.UUID

public object OrganizationHostnameApi {
  public data class Create(
    val organizationGuid: UUID,
    @field:Valid override val body: OrganizationHostnameRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationGuid/hostnames"
  }

  public data class Get(val organizationGuid: UUID, val hostnameGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/hostnames/$hostnameGuid"
  }

  public data class Delete(val organizationGuid: UUID, val hostnameGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/organizations/$organizationGuid/hostnames/$hostnameGuid"
  }
}
