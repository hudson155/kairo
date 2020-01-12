package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.google.inject.name.Named
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import java.util.UUID

internal class FormTemplateServiceImpl @Inject constructor(
    @Store private val formTemplateStore: FormTemplateService
) : FormTemplateService {

    override fun create(model: FormTemplateModel) = formTemplateStore.create(model)

    override fun get(formTemplateId: UUID) = formTemplateStore.get(formTemplateId)

    override fun getByOrgId(orgId: UUID) = formTemplateStore.getByOrgId(orgId)

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update) =
        formTemplateStore.update(formTemplateId, update)

    override fun delete(formTemplateId: UUID) = formTemplateStore.delete(formTemplateId)
}
