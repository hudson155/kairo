package io.limberapp.mapper.org

import com.google.inject.Inject
import io.limberapp.model.org.OrgRoleModel
import io.limberapp.rep.org.OrgRoleRep
import io.limberapp.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime

internal class OrgRoleMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
) {
  fun model(rep: OrgRoleRep.Creation): OrgRoleModel =
      OrgRoleModel(
          guid = uuidGenerator.generate(),
          createdDate = ZonedDateTime.now(clock),
          orgGuid = rep.orgGuid,
          name = rep.name,
          permissions = rep.permissions,
          isDefault = rep.isDefault,
          memberCount = 0,
      )

  fun completeRep(model: OrgRoleModel): OrgRoleRep.Complete =
      OrgRoleRep.Complete(
          guid = model.guid,
          orgGuid = model.orgGuid,
          name = model.name,
          slug = model.slug,
          permissions = model.permissions,
          isDefault = model.isDefault,
          memberCount = model.memberCount,
      )

  fun update(rep: OrgRoleRep.Update): OrgRoleModel.Update =
      OrgRoleModel.Update(
          name = rep.name,
          permissions = rep.permissions,
          isDefault = rep.isDefault,
      )
}
