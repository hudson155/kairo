package io.limberapp.backend.module.auth.client.feature.role

import io.limberapp.backend.module.auth.api.feature.role.FeatureRoleApi
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep

interface FeatureRoleClient {
  suspend operator fun invoke(endpoint: FeatureRoleApi.Post): Result<FeatureRoleRep.Complete>

  suspend operator fun invoke(endpoint: FeatureRoleApi.GetByFeatureGuid): Result<Set<FeatureRoleRep.Complete>>

  suspend operator fun invoke(endpoint: FeatureRoleApi.Patch): Result<FeatureRoleRep.Complete>

  suspend operator fun invoke(endpoint: FeatureRoleApi.Delete): Result<Unit>
}
