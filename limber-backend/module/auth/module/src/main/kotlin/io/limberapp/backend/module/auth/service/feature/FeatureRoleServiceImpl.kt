package io.limberapp.backend.module.auth.service.feature

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.backend.module.auth.store.feature.FeatureRoleStore
import java.util.*

internal class FeatureRoleServiceImpl @Inject constructor(
  private val featureRoleStore: FeatureRoleStore,
) : FeatureRoleService {
  override fun create(model: FeatureRoleModel) =
    featureRoleStore.create(model)

  override fun getByFeatureGuidAndOrgRoleGuids(featureGuid: UUID, orgRoleGuids: Set<UUID>) =
    featureRoleStore.getByFeatureGuidAndOrgRoleGuids(featureGuid, orgRoleGuids)

  override fun getByFeatureGuid(featureGuid: UUID) =
    featureRoleStore.getByFeatureGuid(featureGuid)

  override fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update) =
    featureRoleStore.update(featureGuid, featureRoleGuid, update)

  override fun delete(featureGuid: UUID, featureRoleGuid: UUID) =
    featureRoleStore.delete(featureGuid, featureRoleGuid)
}
