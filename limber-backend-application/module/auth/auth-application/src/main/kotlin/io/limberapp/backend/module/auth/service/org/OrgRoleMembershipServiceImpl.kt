package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import com.piperframework.util.ifNull
import com.piperframework.util.singleNullOrThrow
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
    orgRoleStore.get(orgGuid = orgGuid, orgRoleGuid = model.orgRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw OrgRoleNotFound() }
    return orgRoleMembershipStore.create(model)
  }

  override fun getByOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID): Set<OrgRoleMembershipModel> {
    orgRoleStore.get(orgGuid = orgGuid, orgRoleGuid = orgRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw OrgRoleNotFound() }
    return orgRoleMembershipStore.get(orgRoleGuid = orgRoleGuid)
  }

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID) {
    orgRoleStore.get(orgGuid = orgGuid, orgRoleGuid = orgRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw OrgRoleNotFound() }
    orgRoleMembershipStore.delete(orgRoleGuid, accountGuid)
  }
}
