package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.util.UUID

interface FormInstanceQuestionService {

    fun create(formInstanceId: UUID, models: Set<FormInstanceQuestionModel>)

    fun upsert(formInstanceId: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel

    fun get(formInstanceId: UUID, formTemplateQuestionId: UUID): FormInstanceQuestionModel?

    fun getByFormInstanceId(formInstanceId: UUID): List<FormInstanceQuestionModel>

    fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID)
}
