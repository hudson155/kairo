package io.limberapp.backend.module.forms.mapper.app.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.FormTemplateEntity
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel

internal class FormTemplateMapper @Inject constructor(
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) {

    fun entity(model: FormTemplateModel) = FormTemplateEntity(
        id = model.id,
        created = model.created,
        orgId = model.orgId,
        title = model.title,
        description = model.description,
        questions = model.questions.map { formTemplateQuestionMapper.entity(it) }
    )

    fun model(entity: FormTemplateEntity) = FormTemplateModel(
        id = entity.id,
        created = entity.created,
        orgId = entity.orgId,
        title = entity.title,
        description = entity.description,
        questions = entity.questions.map { formTemplateQuestionMapper.model(it) }
    )

    fun update(model: FormTemplateModel.Update) = FormTemplateEntity.Update(
        title = model.title,
        description = model.description
    )
}
