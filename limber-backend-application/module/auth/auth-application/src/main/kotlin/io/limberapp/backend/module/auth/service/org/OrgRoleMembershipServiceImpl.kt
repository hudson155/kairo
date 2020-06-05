package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import io.limberapp.backend.module.auth.store.org.OrgRoleMembershipStore
import io.limberapp.backend.module.auth.store.org.OrgRoleStore
import java.util.*

internal class OrgRoleMembershipServiceImpl @Inject constructor(
  private val orgRoleStore: OrgRoleStore,
  private val orgRoleMembershipStore: OrgRoleMembershipStore
) : OrgRoleMembershipService {
  override fun create(orgGuid: UUID, model: OrgRoleMembershipModel): OrgRoleMembershipModel {
    checkOrgRoleGuid(orgGuid, model.orgRoleGuid)
    return orgRoleMembershipStore.create(model)
  }

  override fun getByOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID): Set<OrgRoleMembershipModel> {
    checkOrgRoleGuid(orgGuid, orgRoleGuid)
    return orgRoleMembershipStore.getByOrgRoleGuid(orgRoleGuid)
  }

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID) {
    checkOrgRoleGuid(orgGuid, orgRoleGuid)
    orgRoleMembershipStore.delete(orgRoleGuid, accountGuid)
  }

  private fun checkOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID) {
    if (orgRoleStore.get(orgRoleGuid)?.orgGuid != orgGuid) throw OrgRoleNotFound()
  }
}
