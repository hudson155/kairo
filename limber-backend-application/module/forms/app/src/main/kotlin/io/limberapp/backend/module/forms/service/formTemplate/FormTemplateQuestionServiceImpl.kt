package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.mapper.app.formTemplate.FormTemplateQuestionMapper
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import java.util.UUID

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    private val formTemplateQuestionStore: FormTemplateQuestionStore,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper
) : FormTemplateQuestionService {

    override fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        model: FormTemplateQuestionModel,
        index: Int?
    ) {
        val entity = formTemplateQuestionMapper.entity(model)
        formTemplateQuestionStore.create(formTemplateId, formTemplatePartId, formTemplateQuestionGroupId, entity)
    }

    override fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel {
        val entity = formTemplateQuestionStore.update(
            formTemplateId = formTemplateId,
            formTemplatePartId = formTemplatePartId,
            formTemplateQuestionGroupId = formTemplateQuestionGroupId,
            formTemplateQuestionId = formTemplateQuestionId,
            update = formTemplateQuestionMapper.update(update)
        )
        return formTemplateQuestionMapper.model(entity)
    }

    override fun delete(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ) {
        formTemplateQuestionStore.delete(
            formTemplateId = formTemplateId,
            formTemplatePartId = formTemplatePartId,
            formTemplateQuestionGroupId = formTemplateQuestionGroupId,
            formTemplateQuestionId = formTemplateQuestionId
        )
    }
}
