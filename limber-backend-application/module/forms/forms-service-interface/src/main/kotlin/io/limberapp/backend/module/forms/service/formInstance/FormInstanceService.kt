package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.*

interface FormInstanceService {
  fun create(model: FormInstanceModel): FormInstanceModel

  fun get(featureGuid: UUID, formInstanceGuid: UUID): FormInstanceModel?

  fun getByFeatureGuid(featureGuid: UUID, creatorAccountGuid: UUID? = null): Set<FormInstanceModel>

  fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel

  fun delete(featureGuid: UUID, formInstanceGuid: UUID)
}
