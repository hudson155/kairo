package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import java.util.UUID

interface FormTemplateService {

    fun create(model: FormTemplateModel)

    fun get(formTemplateId: UUID): FormTemplateModel?

    fun getByFeatureId(featureId: UUID): Set<FormTemplateModel>

    fun update(formTemplateId: UUID, update: FormTemplateModel.Update): FormTemplateModel

    fun delete(formTemplateId: UUID)
}
