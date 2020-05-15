package io.limberapp.backend.module.forms.authorization

import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import java.util.UUID

internal class HasAccessToFormTemplate(
  private val formTemplateService: FormTemplateService,
  private val formTemplateGuid: UUID?
) : Authorization() {
  override fun authorizeInternal(principal: Jwt?): Boolean {
    principal ?: return false
    formTemplateGuid ?: return false
    if (!AnyJwt.authorize(principal)) return false
    val existingModel = formTemplateService.get(formTemplateGuid) ?: throw FormTemplateNotFound()
    return HasAccessToFeature(existingModel.featureGuid).authorize(principal)
  }
}
