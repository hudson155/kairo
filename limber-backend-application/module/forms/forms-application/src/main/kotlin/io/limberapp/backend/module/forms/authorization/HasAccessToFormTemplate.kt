package io.limberapp.backend.module.forms.authorization

import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

internal class HasAccessToFormTemplate(
    private val formTemplateService: FormTemplateService,
    private val formTemplateId: UUID?
) : Authorization() {
    override fun authorizeInternal(principal: Jwt?): Boolean {
        principal ?: return false
        formTemplateId ?: return false
        if (!AnyJwt.authorize(principal)) return false
        val existingModel = formTemplateService.get(formTemplateId) ?: throw FormTemplateNotFound()
        return HasAccessToFeature(existingModel.featureId).authorize(principal)
    }
}
