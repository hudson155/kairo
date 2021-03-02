package io.limberapp.service.org

import com.google.inject.Inject
import io.limberapp.model.org.OrgRoleModel
import io.limberapp.store.org.OrgRoleStore
import java.util.UUID

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgRoleStore: OrgRoleStore,
) : OrgRoleService {
  override fun create(model: OrgRoleModel): OrgRoleModel =
      orgRoleStore.create(model)

  override fun get(orgRoleGuid: UUID): OrgRoleModel? =
      orgRoleStore[orgRoleGuid]

  override fun getByUserGuid(orgGuid: UUID, userGuid: UUID): Set<OrgRoleModel> =
      orgRoleStore.getByUserGuid(orgGuid, userGuid)

  override fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> =
      orgRoleStore.getByOrgGuid(orgGuid)

  override fun update(orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel =
      orgRoleStore.update(orgRoleGuid, update)

  override fun delete(orgRoleGuid: UUID): Unit =
      orgRoleStore.delete(orgRoleGuid)
}
