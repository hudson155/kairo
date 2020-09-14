package io.limberapp.backend.module.auth.service.org

import com.piperframework.finder.Finder
import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.auth.model.org.OrgRoleFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import java.util.*

@LimberModule.Auth
interface OrgRoleService : Finder<OrgRoleModel, OrgRoleFinder> {
  fun create(model: OrgRoleModel): OrgRoleModel

  fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel

  fun delete(orgGuid: UUID, orgRoleGuid: UUID)
}
