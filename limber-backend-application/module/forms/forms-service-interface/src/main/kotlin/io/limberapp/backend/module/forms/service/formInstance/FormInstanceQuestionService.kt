package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.util.UUID

interface FormInstanceQuestionService {
    fun upsert(model: FormInstanceQuestionModel): FormInstanceQuestionModel

    fun getByFormInstanceGuid(formInstanceGuid: UUID): List<FormInstanceQuestionModel>

    fun delete(formInstanceGuid: UUID, questionGuid: UUID)
}
