package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import java.util.UUID

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    @Store private val formTemplateQuestionStore: FormTemplateQuestionService
) : FormTemplateQuestionService {

    override fun create(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        model: FormTemplateQuestionModel,
        index: Int?
    ) = formTemplateQuestionStore.create(formTemplateId, formTemplatePartId, formTemplateQuestionGroupId, model)

    override fun get(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ) = formTemplateQuestionStore.get(
        formTemplateId = formTemplateId,
        formTemplatePartId = formTemplatePartId,
        formTemplateQuestionGroupId = formTemplateQuestionGroupId,
        formTemplateQuestionId = formTemplateQuestionId
    )

    override fun update(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID,
        update: FormTemplateQuestionModel.Update
    ): FormTemplateQuestionModel = formTemplateQuestionStore.update(
        formTemplateId = formTemplateId,
        formTemplatePartId = formTemplatePartId,
        formTemplateQuestionGroupId = formTemplateQuestionGroupId,
        formTemplateQuestionId = formTemplateQuestionId,
        update = update
    )

    override fun delete(
        formTemplateId: UUID,
        formTemplatePartId: UUID,
        formTemplateQuestionGroupId: UUID,
        formTemplateQuestionId: UUID
    ) = formTemplateQuestionStore.delete(
        formTemplateId = formTemplateId,
        formTemplatePartId = formTemplatePartId,
        formTemplateQuestionGroupId = formTemplateQuestionGroupId,
        formTemplateQuestionId = formTemplateQuestionId
    )
}
