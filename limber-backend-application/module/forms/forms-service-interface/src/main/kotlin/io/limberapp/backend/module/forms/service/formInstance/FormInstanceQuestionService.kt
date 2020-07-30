package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import java.util.*

@LimberModule.Forms
interface FormInstanceQuestionService {
  fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel

  fun getByFormInstanceGuid(featureGuid: UUID, formInstanceGuid: UUID): List<FormInstanceQuestionModel>

  fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID)
}
