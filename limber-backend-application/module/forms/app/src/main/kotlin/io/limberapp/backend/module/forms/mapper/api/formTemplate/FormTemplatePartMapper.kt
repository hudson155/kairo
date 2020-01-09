package io.limberapp.backend.module.forms.mapper.api.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplatePartModel
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplatePartRep

internal class FormTemplatePartMapper @Inject constructor(
    private val uuidGenerator: UuidGenerator,
    private val formTemplateQuestionGroupMapper: FormTemplateQuestionGroupMapper
) {

    fun defaultModel() = FormTemplatePartModel(
        id = uuidGenerator.generate(),
        title = null,
        description = null,
        questionGroups = listOf(formTemplateQuestionGroupMapper.defaultModel())
    )

    fun model(rep: FormTemplatePartRep.Creation) = FormTemplatePartModel(
        id = uuidGenerator.generate(),
        title = rep.title,
        description = rep.description,
        questionGroups = listOf(formTemplateQuestionGroupMapper.defaultModel())
    )

    fun completeRep(model: FormTemplatePartModel) = FormTemplatePartRep.Complete(
        id = model.id,
        title = model.title,
        description = model.description,
        questionGroups = model.questionGroups.map { formTemplateQuestionGroupMapper.completeRep(it) }
    )

    fun update(rep: FormTemplatePartRep.Update) = FormTemplatePartModel.Update(
        title = rep.title,
        description = rep.description
    )
}
