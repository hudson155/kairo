package io.limberapp.backend.module.forms.mapper.api.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionGroupRep
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionGroupModel

internal class FormTemplateQuestionGroupMapper @Inject constructor(
    private val uuidGenerator: UuidGenerator,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) {

    fun defaultModel() = FormTemplateQuestionGroupModel(
        id = uuidGenerator.generate(),
        questions = formTemplateQuestionMapper.defaultModels()
    )

    fun completeRep(model: FormTemplateQuestionGroupModel) = FormTemplateQuestionGroupRep.Complete(
        id = model.id,
        questions = model.questions.map { formTemplateQuestionMapper.completeRep(it) }
    )
}
