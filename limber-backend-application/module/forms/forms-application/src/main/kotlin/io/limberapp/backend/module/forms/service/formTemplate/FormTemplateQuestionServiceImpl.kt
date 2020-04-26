package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import java.util.UUID

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    private val formTemplateQuestionStore: FormTemplateQuestionStore
) : FormTemplateQuestionService {
    override fun create(formTemplateGuid: UUID, model: FormTemplateQuestionModel, rank: Int?) =
        formTemplateQuestionStore.create(formTemplateGuid, model, rank)

    override fun update(formTemplateGuid: UUID, questionGuid: UUID, update: FormTemplateQuestionModel.Update) =
        formTemplateQuestionStore.update(formTemplateGuid, questionGuid, update)

    override fun delete(formTemplateGuid: UUID, questionGuid: UUID) =
        formTemplateQuestionStore.delete(formTemplateGuid, questionGuid)
}
