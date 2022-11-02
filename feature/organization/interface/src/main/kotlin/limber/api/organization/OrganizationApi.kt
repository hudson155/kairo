package limber.api.organization

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.organization.OrganizationRep
import java.util.UUID
import kotlin.reflect.KProperty0

public object OrganizationApi {
  public data class Get(val organizationGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid"
  }

  public data class GetByHostname(val hostname: String) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations"
    override val qp: List<KProperty0<String>> = listOf(::hostname)
  }

  public data class Create(
    @field:Valid override val body: OrganizationRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations"
  }

  public data class Update(
    val organizationGuid: UUID,
    @field:Valid override val body: OrganizationRep.Updater,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Patch
    override val path: String = "/organizations/$organizationGuid"
  }
}
