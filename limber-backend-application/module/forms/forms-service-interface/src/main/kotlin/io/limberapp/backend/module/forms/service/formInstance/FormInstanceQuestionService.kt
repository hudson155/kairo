package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.util.UUID

interface FormInstanceQuestionService {
    fun upsert(formInstanceGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel

    fun delete(formInstanceGuid: UUID, questionGuid: UUID)
}
