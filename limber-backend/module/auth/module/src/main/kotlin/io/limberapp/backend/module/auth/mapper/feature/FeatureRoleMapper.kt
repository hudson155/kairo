package io.limberapp.backend.module.auth.mapper.feature

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.feature.FeatureRoleModel
import io.limberapp.backend.module.auth.rep.feature.FeatureRoleRep
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FeatureRoleMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(featureGuid: UUID, rep: FeatureRoleRep.Creation) = FeatureRoleModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      featureGuid = featureGuid,
      orgRoleGuid = rep.orgRoleGuid,
      permissions = rep.permissions,
  )

  fun completeRep(model: FeatureRoleModel) = FeatureRoleRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      orgRoleGuid = model.orgRoleGuid,
      permissions = model.permissions,
  )

  fun update(rep: FeatureRoleRep.Update) = FeatureRoleModel.Update(
      permissions = rep.permissions,
  )
}
