package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplatePartMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplatePartStore
import java.util.UUID

internal class FormTemplatePartServiceImpl @Inject constructor(
    private val formTemplatePartStore: FormTemplatePartStore,
    private val formTemplatePartMapper: FormTemplatePartMapper
) : FormTemplatePartService {

    override fun create(formTemplateId: UUID, model: FormTemplatePartModel, index: Short?) {
        val entity = formTemplatePartMapper.entity(model)
        formTemplatePartStore.create(formTemplateId, entity)
    }

    override fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        update: FormTemplatePartModel.Update
    ): FormTemplatePartModel {
        val entity = formTemplatePartStore.update(
            formTemplateId = formTemplateId,
            formTemplatePartId = formTemplatePartId,
            update = formTemplatePartMapper.update(update)
        )
        return formTemplatePartMapper.model(entity)
    }

    override fun delete(formTemplateId: UUID, formTemplatePartId: UUID) {
        formTemplatePartStore.delete(formTemplateId, formTemplatePartId)
    }
}
