package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import java.util.*

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    private val formTemplateQuestionStore: FormTemplateQuestionStore,
) : FormTemplateQuestionService {
  override fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int?) =
      formTemplateQuestionStore.create(featureGuid, model, rank)

  override fun get(featureGuid: UUID, formTemplateGuid: UUID, questionGuid: UUID) =
      formTemplateQuestionStore.get(featureGuid, formTemplateGuid, questionGuid)

  override fun getByFormTemplateGuid(featureGuid: UUID, formTemplateGuid: UUID) =
      formTemplateQuestionStore.getByFormTemplateGuid(featureGuid, formTemplateGuid)

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
