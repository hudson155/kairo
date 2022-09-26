package limber.api

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.rep.OrganizationRep
import limber.rest.RestEndpoint
import java.util.UUID

public object OrganizationApi {
  public data class Create(
    @field:Valid override val body: OrganizationRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations"
  }

  public data class Get(val organizationGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid"
  }
}
