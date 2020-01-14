package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.forms.exception.notFound.FormTemplateNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    @Store private val formTemplateStore: FormTemplateService,
    @Store private val formTemplateQuestionStore: FormTemplateQuestionService
) : FormTemplateQuestionService by formTemplateQuestionStore {

    override fun create(formTemplateId: UUID, model: FormTemplateQuestionModel, rank: Int?) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        formTemplateQuestionStore.create(formTemplateId, model, rank)
    }

    override fun update(
        formTemplateId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        return formTemplateQuestionStore.update(formTemplateId, formTemplateQuestionId, update)
    }

    override fun delete(formTemplateId: UUID, formTemplateQuestionId: UUID) {
        formTemplateStore.get(formTemplateId) ?: throw FormTemplateNotFound()
        return formTemplateQuestionStore.delete(formTemplateId, formTemplateQuestionId)
    }
}
