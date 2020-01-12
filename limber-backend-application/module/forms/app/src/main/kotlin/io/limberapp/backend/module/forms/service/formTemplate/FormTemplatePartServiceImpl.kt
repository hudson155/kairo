package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import java.util.UUID

internal class FormTemplatePartServiceImpl @Inject constructor(
    @Store private val formTemplatePartStore: FormTemplatePartService
) : FormTemplatePartService {

    override fun create(formTemplateId: UUID, model: FormTemplatePartModel, index: Int?) =
        formTemplatePartStore.create(formTemplateId, model)

    override fun get(formTemplateId: UUID, formTemplatePartId: UUID) =
        formTemplatePartStore.get(formTemplateId, formTemplatePartId)

    override fun update(formTemplateId: UUID, formTemplatePartId: UUID, update: FormTemplatePartModel.Update) =
        formTemplatePartStore.update(
            formTemplateId = formTemplateId,
            formTemplatePartId = formTemplatePartId,
            update = update
        )

    override fun delete(formTemplateId: UUID, formTemplatePartId: UUID) =
        formTemplatePartStore.delete(formTemplateId, formTemplatePartId)
}
