package limber.api.feature

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.feature.FeatureRep
import java.util.UUID

public object FeatureApi {
  public data class Create(
    val organizationGuid: UUID,
    @field:Valid override val body: FeatureRep.Creator,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationGuid/features"
  }

  public data class Get(val organizationGuid: UUID, val featureGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/features/$featureGuid"
  }

  public data class GetByOrganization(val organizationGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/features"
  }

  public data class Update(
    val organizationGuid: UUID,
    val featureGuid: UUID,
    @field:Valid override val body: FeatureRep.Updater,
  ) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Patch
    override val path: String = "/organizations/$organizationGuid/features/$featureGuid"
  }

  public data class Delete(val organizationGuid: UUID, val featureGuid: UUID) : RestEndpoint() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/organizations/$organizationGuid/features/$featureGuid"
  }
}
