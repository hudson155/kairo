package io.limberapp.backend.module.forms.service.formInstance

import io.limberapp.backend.LimberModule
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.common.finder.Finder
import java.util.*

@LimberModule.Forms
interface FormInstanceQuestionService : Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> {
  fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel

  fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID)
}
