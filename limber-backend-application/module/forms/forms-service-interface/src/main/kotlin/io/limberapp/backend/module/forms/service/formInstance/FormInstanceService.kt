package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.*

interface FormInstanceService {
  fun create(model: FormInstanceModel): FormInstanceModel

  fun get(formInstanceGuid: UUID): FormInstanceModel?

  fun getByFeatureGuid(featureGuid: UUID): Set<FormInstanceModel>

  fun delete(formInstanceGuid: UUID)
}
