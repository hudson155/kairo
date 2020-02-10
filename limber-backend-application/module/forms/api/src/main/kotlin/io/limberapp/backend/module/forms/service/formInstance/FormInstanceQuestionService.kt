package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.util.UUID

interface FormInstanceQuestionService {

    fun upsert(formInstanceId: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel

    fun delete(formInstanceId: UUID, formTemplateQuestionId: UUID)
}
