package io.limberapp.backend.module.orgs.service.org

import com.google.inject.Inject
import io.limberapp.backend.module.orgs.exception.org.OrgNotFound
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import io.limberapp.backend.module.orgs.store.org.OrgRoleStore
import java.util.UUID

internal class OrgRoleServiceImpl @Inject constructor(
    private val orgService: OrgService,
    private val orgRoleStore: OrgRoleStore
) : OrgRoleService {
    override fun create(orgGuid: UUID, model: OrgRoleModel) = orgRoleStore.create(orgGuid, model)

    override fun getByOrgGuid(orgGuid: UUID): Set<OrgRoleModel> {
        orgService.get(orgGuid) ?: throw OrgNotFound()
        return orgRoleStore.getByOrgGuid(orgGuid)
    }

    override fun update(orgGuid: UUID, orgRoleGuid: UUID, update: OrgRoleModel.Update) =
        orgRoleStore.update(orgGuid, orgRoleGuid, update)

    override fun delete(orgGuid: UUID, orgRoleGuid: UUID) = orgRoleStore.delete(orgGuid, orgRoleGuid)
}
