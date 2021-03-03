package io.limberapp.service.feature

import com.google.inject.Inject
import io.limberapp.model.feature.FeatureRoleModel
import io.limberapp.store.feature.FeatureRoleStore
import java.util.UUID

internal class FeatureRoleServiceImpl @Inject constructor(
    private val featureRoleStore: FeatureRoleStore,
) : FeatureRoleService {
  override fun create(model: FeatureRoleModel): FeatureRoleModel =
      featureRoleStore.create(model)

  override fun get(featureRoleGuid: UUID): FeatureRoleModel? =
      featureRoleStore[featureRoleGuid]

  override fun getByOrgRoleGuids(
      featureGuid: UUID,
      orgRoleGuids: Set<UUID>,
  ): Set<FeatureRoleModel> =
      featureRoleStore.getByOrgRoleGuids(featureGuid, orgRoleGuids)

  override fun getByFeatureGuid(featureGuid: UUID): Set<FeatureRoleModel> =
      featureRoleStore.getByFeatureGuid(featureGuid)

  override fun update(featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel =
      featureRoleStore.update(featureRoleGuid, update)

  override fun delete(featureRoleGuid: UUID): Unit =
      featureRoleStore.delete(featureRoleGuid)
}
