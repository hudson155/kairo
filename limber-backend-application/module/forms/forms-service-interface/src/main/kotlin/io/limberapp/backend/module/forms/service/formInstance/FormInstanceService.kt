package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.UUID

interface FormInstanceService {
  fun create(model: FormInstanceModel)

  fun get(formInstanceGuid: UUID): FormInstanceModel?

  fun getByFeatureGuid(featureGuid: UUID): Set<FormInstanceModel>

  fun delete(formInstanceGuid: UUID)
}
