package io.limberapp.backend.module.auth.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.auth.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.auth.model.org.OrgRoleModel
import io.limberapp.backend.module.auth.store.org.OrgRoleStore
import java.util.UUID

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
    override fun create(model: OrgRoleModel) = orgRoleStore.create(model)

    override fun getByOrgGuid(orgGuid: UUID) = orgRoleStore.getByOrgGuid(orgGuid)

    override fun getByAccountGuid(orgGuid: UUID, accountGuid: UUID) =
        orgRoleStore.getByAccountGuid(orgGuid, accountGuid)

    override fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update): OrgRoleModel {
        checkOrgRoleGuid(orgGuid, orgRoleGuid)
        return orgRoleStore.update(orgRoleGuid, update)
    }

    override fun delete(orgGuid: UUID, orgRoleGuid: UUID) {
        checkOrgRoleGuid(orgGuid, orgRoleGuid)
        orgRoleStore.delete(orgRoleGuid)
    }

    private fun checkOrgRoleGuid(orgGuid: UUID, orgRoleGuid: UUID) {
        if (orgRoleStore.get(orgRoleGuid)?.orgGuid != orgGuid) throw OrgRoleNotFound()
    }
}
