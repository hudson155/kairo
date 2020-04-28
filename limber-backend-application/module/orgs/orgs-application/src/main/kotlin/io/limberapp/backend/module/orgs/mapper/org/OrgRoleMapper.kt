package io.limberapp.backend.module.orgs.mapper.org

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.authorization.permissions.OrgPermissions
import io.limberapp.backend.module.orgs.model.org.OrgRoleModel
import io.limberapp.backend.module.orgs.rep.org.OrgRoleRep
import java.time.Clock
import java.time.LocalDateTime

internal class OrgRoleMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator
) {

    fun model(rep: OrgRoleRep.Creation) = OrgRoleModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        name = rep.name,
        permissions = OrgPermissions.none()
    )

    fun completeRep(model: OrgRoleModel) = OrgRoleRep.Complete(
        guid = model.guid,
        createdDate = model.createdDate,
        name = model.name,
        permissions = model.permissions
    )

    fun update(rep: OrgRoleRep.Update) = OrgRoleModel.Update(
        name = rep.name,
        permissions = rep.permissions
    )
}
