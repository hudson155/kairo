package limber.api.feature

import io.ktor.http.HttpMethod
import jakarta.validation.Valid
import limber.feature.rest.RestEndpoint
import limber.rep.feature.FeatureRep
import java.util.UUID

public object FeatureApi {
  public data class Get(
    val featureId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/features/$featureId"
  }

  public data class ListByOrganization(
    val organizationGuid: UUID,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/organizations/$organizationGuid/features"
  }

  public data class Create(
    val organizationGuid: UUID,
    @Valid override val body: FeatureRep.Creator?,
  ) : RestEndpoint<FeatureRep.Creator>() {
    override val method: HttpMethod = HttpMethod.Post
    override val path: String = "/organizations/$organizationGuid/features"
  }

  public data class Update(
    val featureId: String,
    @Valid override val body: FeatureRep.Update?,
  ) : RestEndpoint<FeatureRep.Update>() {
    override val method: HttpMethod = HttpMethod.Patch
    override val path: String = "/features/$featureId"
  }

  public data class Delete(
    val featureId: String,
  ) : RestEndpoint<Nothing>() {
    override val method: HttpMethod = HttpMethod.Delete
    override val path: String = "/features/$featureId"
  }
}
