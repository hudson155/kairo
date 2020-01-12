package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.entity.FormTemplatePartEntity
import java.util.UUID

internal interface FormTemplatePartStore : Store {

    fun create(formTemplateId: UUID, entity: FormTemplatePartEntity)

    fun get(formTemplateId: UUID, formTemplatePartId: UUID): FormTemplatePartEntity?

    fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        update: FormTemplatePartEntity.Update
    ): FormTemplatePartEntity

    fun delete(formTemplateId: UUID, formTemplatePartId: UUID)
}
