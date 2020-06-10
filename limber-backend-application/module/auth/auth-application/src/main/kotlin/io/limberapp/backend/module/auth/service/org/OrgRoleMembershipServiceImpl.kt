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
    if (!orgRoleStore.existsAndHasOrgGuid(model.orgRoleGuid, orgGuid = orgGuid)) throw OrgRoleNotFound()
    return orgRoleMembershipStore.create(model)
  }

  override fun getByOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID): Set<OrgRoleMembershipModel> {
    if (!orgRoleStore.existsAndHasOrgGuid(orgRoleGuid, orgGuid = orgGuid)) throw OrgRoleNotFound()
    return orgRoleMembershipStore.getByOrgRoleGuid(orgRoleGuid)
  }

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID) {
    if (!orgRoleStore.existsAndHasOrgGuid(orgRoleGuid, orgGuid = orgGuid)) throw OrgRoleNotFound()
    orgRoleMembershipStore.delete(orgRoleGuid, accountGuid)
  }
}
