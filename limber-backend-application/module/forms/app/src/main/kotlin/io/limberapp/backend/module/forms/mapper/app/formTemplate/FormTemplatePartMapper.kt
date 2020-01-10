package io.limberapp.backend.module.forms.mapper.app.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.FormTemplatePartEntity
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel

internal class FormTemplatePartMapper @Inject constructor(
    private val formTemplateQuestionGroupMapper: FormTemplateQuestionGroupMapper
) {

    fun entity(model: FormTemplatePartModel) = FormTemplatePartEntity(
        id = model.id,
        title = model.title,
        description = model.description,
        questionGroups = model.questionGroups.map { formTemplateQuestionGroupMapper.entity(it) }
    )

    fun model(entity: FormTemplatePartEntity) = FormTemplatePartModel(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        questionGroups = entity.questionGroups.map { formTemplateQuestionGroupMapper.model(it) }
    )

    fun update(model: FormTemplatePartModel.Update) = FormTemplatePartEntity.Update(
        title = model.title,
        description = model.description
    )
}
