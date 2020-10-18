package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.store.org.OrgRoleStore
import java.util.*

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgRoleStore: OrgRoleStore,
) : OrgRoleService {
  override fun create(model: OrgRoleModel) =
      orgRoleStore.create(model)

  override fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID) =
      orgRoleStore.getByAccountGuid(orgGuid, accountGuid)

  override fun getByOrgGuid(orgGuid: UUID) =
      orgRoleStore.getByOrgGuid(orgGuid)

  override fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update) =
      orgRoleStore.update(orgGuid, orgRoleGuid, update)

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID) =
      orgRoleStore.delete(orgGuid, orgRoleGuid)
}
