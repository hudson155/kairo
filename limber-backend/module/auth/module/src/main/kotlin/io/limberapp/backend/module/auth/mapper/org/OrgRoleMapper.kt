package io.limberapp.backend.module.auth.mapper.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime
import java.util.*

internal class OrgRoleMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(orgGuid: UUID, rep: OrgRoleRep.Creation) = OrgRoleModel(
      guid = uuidGenerator.generate(),
      createdDate = ZonedDateTime.now(clock),
      orgGuid = orgGuid,
      name = rep.name,
      permissions = rep.permissions,
      isDefault = rep.isDefault,
      memberCount = 0,
  )

  fun completeRep(model: OrgRoleModel) = OrgRoleRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      name = model.name,
      slug = model.slug,
      permissions = model.permissions,
      isDefault = model.isDefault,
      memberCount = model.memberCount,
  )

  fun update(rep: OrgRoleRep.Update) = OrgRoleModel.Update(
      name = rep.name,
      permissions = rep.permissions,
      isDefault = rep.isDefault,
  )
}
