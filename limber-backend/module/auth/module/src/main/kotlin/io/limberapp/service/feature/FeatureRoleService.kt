package io.limberapp.service.feature

import io.limberapp.model.feature.FeatureRoleModel
import java.util.UUID

interface FeatureRoleService {
  fun create(model: FeatureRoleModel): FeatureRoleModel

  operator fun get(featureRoleGuid: UUID): FeatureRoleModel?

  fun getByOrgRoleGuids(featureGuid: UUID, orgRoleGuids: Set<UUID>): Set<FeatureRoleModel>

  fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel>

  fun update(featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel

  fun delete(featureRoleGuid: UUID)
}
