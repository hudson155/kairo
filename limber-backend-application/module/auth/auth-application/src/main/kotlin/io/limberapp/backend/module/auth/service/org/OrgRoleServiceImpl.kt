package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import com.piperframework.util.ifNull
import com.piperframework.util.singleNullOrThrow
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.store.org.OrgRoleStore
import java.util.*

internal class OrgRoleServiceImpl @Inject constructor(
  private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
  override fun create(model: OrgRoleModel) = orgRoleStore.create(model)

  override fun getByOrgGuid(orgGuid: UUID) = orgRoleStore.get(orgGuid = orgGuid).toSet()

  override fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID) =
    orgRoleStore.get(orgGuid = orgGuid, accountGuid = accountGuid).toSet()

  override fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel {
    orgRoleStore.get(orgGuid = orgGuid, orgRoleGuid = orgRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw OrgRoleNotFound() }
    return orgRoleStore.update(orgRoleGuid, update)
  }

  override fun delete(orgGuid: UUID, orgRoleGuid: UUID) {
    orgRoleStore.get(orgGuid = orgGuid, orgRoleGuid = orgRoleGuid)
      .singleNullOrThrow()
      .ifNull { throw OrgRoleNotFound() }
    orgRoleStore.delete(orgRoleGuid)
  }
}
