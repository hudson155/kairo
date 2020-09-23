package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipFinder
import io.limberapp.backend.module.auth.model.org.OrgRoleMembershipModel
import io.limberapp.backend.module.auth.store.org.OrgRoleMembershipStore
import io.limberapp.common.finder.Finder
import java.util.*

internal class OrgRoleMembershipServiceImpl @Inject constructor(
  private val orgRoleMembershipStore: OrgRoleMembershipStore,
) : OrgRoleMembershipService, Finder<OrgRoleMembershipModel, OrgRoleMembershipFinder> by orgRoleMembershipStore {
  override fun create(orgGuid: UUID, model: OrgRoleMembershipModel) =
    orgRoleMembershipStore.create(orgGuid, model)

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID, accountGuid: UUID) =
    orgRoleMembershipStore.delete(orgGuid, orgRoleGuid, accountGuid)
}
