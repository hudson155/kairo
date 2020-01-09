package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import java.util.UUID

internal class FormTemplateServiceImpl @Inject constructor(
    private val formTemplateStore: FormTemplateStore,
    private val formTemplateMapper: FormTemplateMapper
) : FormTemplateService {

    override fun create(model: FormTemplateModel) {
        val entity = formTemplateMapper.entity(model)
        formTemplateStore.create(entity)
    }

    override fun get(formTemplateId: UUID): FormTemplateModel? {
        val entity = formTemplateStore.get(formTemplateId) ?: return null
        return formTemplateMapper.model(entity)
    }

    override fun getByOrgId(orgId: UUID): List<FormTemplateModel> {
        val entities = formTemplateStore.getByOrgId(orgId)
        return entities.map { formTemplateMapper.model(it) }
    }

    override fun update(formTemplateId: UUID, update: FormTemplateModel.Update): FormTemplateModel {
        val entity = formTemplateStore.update(formTemplateId, formTemplateMapper.update(update))
        return formTemplateMapper.model(entity)
    }

    override fun delete(formTemplateId: UUID) {
        formTemplateStore.delete(formTemplateId)
    }
}
