package io.limberapp.backend.module.orgs.service.formTemplate

import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateModel
import java.util.UUID

interface FormTemplateService {

    fun create(model: FormTemplateModel)

    fun get(id: UUID): FormTemplateModel?

    fun getByOrgId(orgId: UUID): List<FormTemplateModel>

    fun update(id: UUID, update: FormTemplateModel.Update): FormTemplateModel

    fun delete(id: UUID)
}
