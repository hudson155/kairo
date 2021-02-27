package io.limberapp.endpoint.feature

import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.limberapp.api.feature.FeatureApi
import io.limberapp.auth.auth.AuthSuperuser
import io.limberapp.restInterface.EndpointHandler
import io.limberapp.restInterface.template
import io.limberapp.service.feature.FeatureService
import java.util.UUID

internal class DeleteFeature @Inject constructor(
    private val featureService: FeatureService,
) : EndpointHandler<FeatureApi.Delete, Unit>(
    template = FeatureApi.Delete::class.template(),
) {
  override suspend fun endpoint(call: ApplicationCall): FeatureApi.Delete =
      FeatureApi.Delete(featureGuid = call.getParam(UUID::class, "featureGuid"))

  override suspend fun Handler.handle(endpoint: FeatureApi.Delete) {
    auth(AuthSuperuser)
    featureService.delete(endpoint.featureGuid)
  }
}
