package io.limberapp.backend.module.forms.store.formTemplate

import com.piperframework.store.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import java.util.UUID

internal interface FormTemplateStore : Store {

    fun create(model: FormTemplateModel)

    fun get(formTemplateId: UUID): FormTemplateModel?

    fun getByOrgId(orgId: UUID): List<FormTemplateModel>

    fun update(formTemplateId: UUID, update: FormTemplateModel.Update): FormTemplateModel

    fun delete(formTemplateId: UUID)
}
