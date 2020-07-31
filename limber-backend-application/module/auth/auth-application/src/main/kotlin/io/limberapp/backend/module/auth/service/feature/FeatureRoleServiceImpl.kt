package io.limberapp.backend.module.auth.service.feature

import com.google.inject.Inject
import com.piperframework.util.ifNull
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.exception.feature.FeatureRoleNotFound
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.backend.module.auth.store.feature.FeatureRoleStore
import java.util.*

internal class FeatureRoleServiceImpl @Inject constructor(
  private val featureRoleStore: FeatureRoleStore
) : FeatureRoleService {
  override fun create(model: FeatureRoleModel) = featureRoleStore.create(model)

  override fun getByFeatureGuid(featureGuid: UUID) = featureRoleStore.get(featureGuid = featureGuid).toSet()

  override fun update(featureGuid: UUID, featureRoleGuid: UUID, update: FeatureRoleModel.Update): FeatureRoleModel {
    featureRoleStore.get(featureGuid = featureGuid, featureRoleGuid = featureRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw FeatureRoleNotFound() }
    return featureRoleStore.update(featureRoleGuid, update)
  }

  override fun delete(featureGuid: UUID, featureRoleGuid: UUID) {
    featureRoleStore.get(featureGuid = featureGuid, featureRoleGuid = featureRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw FeatureRoleNotFound() }
    featureRoleStore.delete(featureRoleGuid)
  }
}
