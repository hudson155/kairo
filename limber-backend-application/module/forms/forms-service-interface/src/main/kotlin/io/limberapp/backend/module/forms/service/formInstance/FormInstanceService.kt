package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import java.util.*

interface FormInstanceService {
  fun create(model: FormInstanceModel): FormInstanceModel

  fun get(featureGuid: UUID, formInstanceGuid: UUID): FormInstanceModel?

  fun getByFeatureGuidAndCreatorAccountGuid(featureGuid: UUID, creatorAccountGuid: UUID): Set<FormInstanceModel>

  fun getByFeatureGuid(featureGuid: UUID): Set<FormInstanceModel>

  fun delete(featureGuid: UUID, formInstanceGuid: UUID)
}
