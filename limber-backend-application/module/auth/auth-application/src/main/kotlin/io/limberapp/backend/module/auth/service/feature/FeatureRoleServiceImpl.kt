package io.limberapp.backend.module.auth.service.feature

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.auth.model.feature.FeatureRoleFinder
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.backend.module.auth.store.feature.FeatureRoleStore
import java.util.*

internal class FeatureRoleServiceImpl @Inject constructor(
  private val featureRoleStore: FeatureRoleStore,
) : FeatureRoleService, Finder<FeatureRoleModel, FeatureRoleFinder> by featureRoleStore {
  override fun create(model: FeatureRoleModel) =
    featureRoleStore.create(model)

  override fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update) =
    featureRoleStore.update(featureGuid, featureRoleGuid, update)

  override fun delete(featureGuid: UUID, featureRoleGuid: UUID) =
    featureRoleStore.delete(featureGuid, featureRoleGuid)
}
