package io.limberapp.backend.module.forms.service.formTemplate

import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import java.util.UUID

interface FormTemplatePartService {

    fun create(formTemplateId: UUID, model: FormTemplatePartModel, index: Short? = null)

    fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        update: FormTemplatePartModel.Update
    ): FormTemplatePartModel

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID)
}
