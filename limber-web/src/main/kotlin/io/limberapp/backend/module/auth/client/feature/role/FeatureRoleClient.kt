package io.limberapp.backend.module.auth.client.feature.role

import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep

interface FeatureRoleClient {
  suspend operator fun invoke(endpoint: FeatureRoleApi.Post): Outcome<FeatureRoleRep.Complete>

  suspend operator fun invoke(endpoint: FeatureRoleApi.GetByFeatureGuid): Outcome<Set<FeatureRoleRep.Complete>>

  suspend operator fun invoke(endpoint: FeatureRoleApi.Patch): Outcome<FeatureRoleRep.Complete>

  suspend operator fun invoke(endpoint: FeatureRoleApi.Delete): Outcome<Unit>
}
