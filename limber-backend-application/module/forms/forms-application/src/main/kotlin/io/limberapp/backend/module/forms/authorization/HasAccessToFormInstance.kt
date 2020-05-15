package io.limberapp.backend.module.forms.authorization

import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.authorization.principal.Jwt
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.*

internal class HasAccessToFormInstance(
  private val formInstanceService: FormInstanceService,
  private val formInstanceGuid: UUID?
) : Authorization() {
  override fun authorizeInternal(principal: Jwt?): Boolean {
    principal ?: return false
    formInstanceGuid ?: return false
    if (!AnyJwt.authorize(principal)) return false
    val existingModel = formInstanceService.get(formInstanceGuid) ?: throw FormInstanceNotFound()
    return HasAccessToFeature(existingModel.featureGuid).authorize(principal)
  }
}
