package io.limberapp.backend.module.orgs.service.formTemplate

import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplatePartModel
import java.util.UUID

interface FormTemplatePartService {

    fun create(formTemplateId: UUID, model: FormTemplatePartModel, index: Short? = null)

    fun update(formTemplateId: UUID, id: UUID, update: FormTemplatePartModel.Update): FormTemplatePartModel

    fun delete(formTemplateId: UUID, id: UUID)
}
