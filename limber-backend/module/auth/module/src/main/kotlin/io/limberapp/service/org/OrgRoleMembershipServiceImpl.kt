package io.limberapp.service.org

import com.google.inject.Inject
import io.limberapp.model.org.OrgRoleMembershipModel
import io.limberapp.store.org.OrgRoleMembershipStore
import java.util.UUID

internal class OrgRoleMembershipServiceImpl @Inject constructor(
    private val orgRoleMembershipStore: OrgRoleMembershipStore,
) : OrgRoleMembershipService {
  override fun create(model: OrgRoleMembershipModel): OrgRoleMembershipModel =
      orgRoleMembershipStore.create(model)

  override fun getByOrgRoleGuid(orgRoleGuid: UUID): Set<OrgRoleMembershipModel> =
      orgRoleMembershipStore.getByOrgRoleGuid(orgRoleGuid)

  override fun delete(orgRoleGuid: UUID, userGuid: UUID): Unit =
      orgRoleMembershipStore.delete(orgRoleGuid, userGuid)
}
