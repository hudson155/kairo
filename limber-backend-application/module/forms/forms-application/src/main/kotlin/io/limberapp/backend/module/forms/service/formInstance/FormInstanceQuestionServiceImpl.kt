package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import java.util.*

internal class FormInstanceQuestionServiceImpl @Inject constructor(
  private val formInstanceQuestionStore: FormInstanceQuestionStore,
) : FormInstanceQuestionService,
  Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> by formInstanceQuestionStore {
  override fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    requireNotNull(model.questionGuid)
    return formInstanceQuestionStore.upsert(featureGuid, model)
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID) =
    formInstanceQuestionStore.delete(featureGuid, formInstanceGuid, questionGuid)
}
