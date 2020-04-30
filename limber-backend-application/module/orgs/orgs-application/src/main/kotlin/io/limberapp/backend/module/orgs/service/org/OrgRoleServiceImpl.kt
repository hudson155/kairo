package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.exception.org.OrgRoleNotFound
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import io.limberapp.backend.module.orgs.store.org.OrgRoleStore
import java.util.UUID

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
    override fun create(model: OrgRoleModel) = orgRoleStore.create(model)

    override fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> {
        orgService.get(orgGuid) ?: throw OrgNotFound()
        return orgRoleStore.getByOrgGuid(orgGuid)
    }

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
