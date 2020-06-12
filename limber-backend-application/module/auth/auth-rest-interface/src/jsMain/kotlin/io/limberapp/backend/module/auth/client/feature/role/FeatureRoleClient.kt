package io.limberapp.backend.module.auth.client.feature.role

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep

class FeatureRoleClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FeatureRoleApi.Post) =
    fetch(endpoint) { json.parse<FeatureRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FeatureRoleApi.GetByFeatureGuid) =
    fetch(endpoint) { json.parseSet<FeatureRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FeatureRoleApi.Patch) =
    fetch(endpoint) { json.parse<FeatureRoleRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FeatureRoleApi.Delete) =
    fetch(endpoint)
}
