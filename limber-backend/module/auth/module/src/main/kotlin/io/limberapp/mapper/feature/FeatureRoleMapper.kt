package io.limberapp.mapper.feature

import com.google.inject.Inject
import io.limberapp.model.feature.FeatureRoleModel
import io.limberapp.rep.feature.FeatureRoleRep
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime

internal class FeatureRoleMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(rep: FeatureRoleRep.Creation): FeatureRoleModel =
      FeatureRoleModel(
          guid = uuidGenerator.generate(),
          createdDate = ZonedDateTime.now(clock),
          featureGuid = rep.featureGuid,
          orgRoleGuid = rep.orgRoleGuid,
          permissions = rep.permissions,
      )

  fun completeRep(model: FeatureRoleModel): FeatureRoleRep.Complete =
      FeatureRoleRep.Complete(
          guid = model.guid,
          featureGuid = model.featureGuid,
          orgRoleGuid = model.orgRoleGuid,
          permissions = model.permissions,
      )

  fun update(rep: FeatureRoleRep.Update): FeatureRoleModel.Update =
      FeatureRoleModel.Update(permissions = rep.permissions)
}
