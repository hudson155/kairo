package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplatePartStore
import java.util.UUID

internal class FormTemplatePartServiceImpl @Inject constructor(
    private val formTemplatePartStore: FormTemplatePartStore
) : FormTemplatePartService {

    override fun create(formTemplateId: UUID, model: FormTemplatePartModel, index: Int?) =
        formTemplatePartStore.create(formTemplateId, model)

    override fun update(formTemplateId: UUID, formTemplatePartId: UUID, update: FormTemplatePartModel.Update) =
        formTemplatePartStore.update(
            formTemplateId = formTemplateId,
            formTemplatePartId = formTemplatePartId,
            update = update
        )

    override fun delete(formTemplateId: UUID, formTemplatePartId: UUID) =
        formTemplatePartStore.delete(formTemplateId, formTemplatePartId)
}
