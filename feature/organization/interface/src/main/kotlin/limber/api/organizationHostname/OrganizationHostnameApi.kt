package limber.api.organizationHostname

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.organizationHostname.OrganizationHostnameRep
import java.util.UUID

public object OrganizationHostnameApi {
  public data class Get(val organizationGuid: UUID, val hostnameGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/hostnames/$hostnameGuid"
  }

  public data class Create(
    val organizationGuid: UUID,
    @Valid override val body: OrganizationHostnameRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationGuid/hostnames"
  }

  public data class Delete(val organizationGuid: UUID, val hostnameGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/organizations/$organizationGuid/hostnames/$hostnameGuid"
  }
}
