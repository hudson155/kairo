package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import java.util.UUID

internal interface FormTemplatePartStore : Store {

    fun create(formTemplateId: UUID, model: FormTemplatePartModel)

    fun get(formTemplateId: UUID, formTemplatePartId: UUID): FormTemplatePartModel?

    fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        update: FormTemplatePartModel.Update
    ): FormTemplatePartModel

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID)
}
