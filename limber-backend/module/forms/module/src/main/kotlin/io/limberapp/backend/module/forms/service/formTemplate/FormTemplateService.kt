package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import java.util.*

interface FormTemplateService {
  fun create(model: FormTemplateModel): FormTemplateModel

  fun get(featureGuid: UUID, formTemplateGuid: UUID): FormTemplateModel?

  fun getByFeatureGuid(featureGuid: UUID): Set<FormTemplateModel>

  fun update(featureGuid: UUID, formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel

  fun delete(featureGuid: UUID, formTemplateGuid: UUID)
}
