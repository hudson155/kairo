package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.store.org.OrgRoleStore
import java.util.*

internal class OrgRoleServiceImpl @Inject constructor(
  private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
  override fun create(model: OrgRoleModel) = orgRoleStore.create(model)

  override fun getByOrgGuid(orgGuid: UUID) = orgRoleStore.getByOrgGuid(orgGuid)

  override fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID) =
    orgRoleStore.getByAccountGuid(orgGuid, accountGuid)

  override fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel {
    if (!orgRoleStore.existsAndHasOrgGuid(orgRoleGuid, orgGuid = orgGuid)) throw OrgRoleNotFound()
    return orgRoleStore.update(orgRoleGuid, update)
  }

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID) {
    if (!orgRoleStore.existsAndHasOrgGuid(orgRoleGuid, orgGuid = orgGuid)) throw OrgRoleNotFound()
    orgRoleStore.delete(orgRoleGuid)
  }
}
