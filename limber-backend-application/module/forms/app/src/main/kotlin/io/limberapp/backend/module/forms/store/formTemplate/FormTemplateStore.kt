package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import java.util.UUID

internal interface FormTemplateStore : Store {

    fun create(entity: FormTemplateEntity)

    fun get(formTemplateId: UUID): FormTemplateEntity?

    fun getByOrgId(orgId: UUID): List<FormTemplateEntity>

    fun update(formTemplateId: UUID, update: FormTemplateEntity.Update): FormTemplateEntity

    fun delete(formTemplateId: UUID)
}
