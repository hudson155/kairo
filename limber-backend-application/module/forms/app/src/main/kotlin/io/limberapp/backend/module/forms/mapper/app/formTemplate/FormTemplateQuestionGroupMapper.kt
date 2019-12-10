package io.limberapp.backend.module.forms.mapper.app.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.entity.FormTemplateQuestionGroupEntity
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionGroupModel

internal class FormTemplateQuestionGroupMapper @Inject constructor(
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) {

    fun entity(model: FormTemplateQuestionGroupModel) = FormTemplateQuestionGroupEntity(
        id = model.id,
        questions = model.questions.map { formTemplateQuestionMapper.entity(it) }
    )

    fun model(entity: FormTemplateQuestionGroupEntity) = FormTemplateQuestionGroupModel(
        id = entity.id,
        questions = entity.questions.map { formTemplateQuestionMapper.model(it) }
    )
}
