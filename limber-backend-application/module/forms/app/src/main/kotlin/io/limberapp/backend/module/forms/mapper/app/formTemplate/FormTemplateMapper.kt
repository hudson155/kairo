package io.limberapp.backend.module.forms.mapper.app.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateModel

internal class FormTemplateMapper @Inject constructor(
    private val formTemplatePartMapper: FormTemplatePartMapper
) {

    fun entity(model: FormTemplateModel) = FormTemplateEntity(
        id = model.id,
        created = model.created,
        orgId = model.orgId,
        title = model.title,
        description = model.description,
        parts = model.parts.map { formTemplatePartMapper.entity(it) }
    )

    fun model(entity: FormTemplateEntity) = FormTemplateModel(
        id = entity.id,
        created = entity.created,
        orgId = entity.orgId,
        title = entity.title,
        description = entity.description,
        parts = entity.parts.map { formTemplatePartMapper.model(it) }
    )

    fun update(model: FormTemplateModel.Update) = FormTemplateEntity.Update(
        title = model.title,
        description = model.description
    )
}
