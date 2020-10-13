package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionFinder
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import io.limberapp.common.finder.Finder
import java.util.*

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    private val formTemplateQuestionStore: FormTemplateQuestionStore,
) : FormTemplateQuestionService,
    Finder<FormTemplateQuestionModel, FormTemplateQuestionFinder> by formTemplateQuestionStore {
  override fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int?) =
      formTemplateQuestionStore.create(featureGuid, model, rank)

  override fun update(
      featureGuid: UUID,
      formTemplateGuid: UUID,
      questionGuid: UUID,
      update: FormTemplateQuestionModel.Update,
  ): FormTemplateQuestionModel =
      formTemplateQuestionStore.update(featureGuid, formTemplateGuid, questionGuid, update)

  override fun delete(featureGuid: UUID, formTemplateGuid: UUID, questionGuid: UUID) =
      formTemplateQuestionStore.delete(featureGuid, formTemplateGuid, questionGuid)
}
