package limber.api.organizationAuth

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.organizationAuth.OrganizationAuthRep
import java.util.UUID

public object OrganizationAuthApi {
  public data class GetByOrganization(val organizationGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/auth"
  }

  public data class Set(
    val organizationGuid: UUID,
    @field:Valid override val body: OrganizationAuthRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Put
    override val path: String = "/organizations/$organizationGuid/auth"
  }

  public data class DeleteByOrganization(val organizationGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/organizations/$organizationGuid/auth"
  }
}
