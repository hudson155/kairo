package io.limberapp.backend.module.forms.authorization

import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

class MemberOfOrgThatOwnsFormInstance(
    private val formInstanceService: FormInstanceService,
    private val formInstanceId: UUID?
) : Authorization() {

    override fun authorizeInternal(principal: Jwt?): Boolean {
        principal ?: return false
        formInstanceId ?: return false
        if (!AnyJwt.authorize(principal)) return false
        val existingModel = formInstanceService.get(formInstanceId) ?: throw FormInstanceNotFound()
        return OrgMember(existingModel.orgId).authorize(principal)
    }
}
