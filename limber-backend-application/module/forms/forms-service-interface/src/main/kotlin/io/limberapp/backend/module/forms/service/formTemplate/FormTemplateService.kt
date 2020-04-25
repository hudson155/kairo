package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import java.util.UUID

interface FormTemplateService {
    fun create(model: FormTemplateModel)

    fun get(formTemplateGuid: UUID): FormTemplateModel?

    fun getByFeatureGuid(featureGuid: UUID): Set<FormTemplateModel>

    fun update(formTemplateGuid: UUID, update: FormTemplateModel.Update): FormTemplateModel

    fun delete(formTemplateGuid: UUID)
}
