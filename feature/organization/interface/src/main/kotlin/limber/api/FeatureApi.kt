package limber.api

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.rep.FeatureRep
import limber.rest.RestEndpoint
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
}
