package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import java.util.*

internal class FormInstanceQuestionServiceImpl @Inject constructor(
  private val formInstanceStore: FormInstanceStore,
  private val formInstanceQuestionStore: FormInstanceQuestionStore,
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper
) : FormInstanceQuestionService {
  override fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    val questionGuid = requireNotNull(model.questionGuid)
    checkFeatureGuid(featureGuid, model.formInstanceGuid)
    val existingFormInstanceQuestionModel = formInstanceQuestionStore.get(model.formInstanceGuid, questionGuid)
    return if (existingFormInstanceQuestionModel == null) {
      formInstanceQuestionStore.create(model)
    } else {
      formInstanceQuestionStore.update(
        formInstanceGuid = model.formInstanceGuid,
        questionGuid = questionGuid,
        update = formInstanceQuestionMapper.update(model)
      )
    }
  }

  override fun getByFormInstanceGuid(featureGuid: UUID, formInstanceGuid: UUID): List<FormInstanceQuestionModel> {
    checkFeatureGuid(featureGuid, formInstanceGuid)
    return formInstanceQuestionStore.getByFormInstanceGuid(formInstanceGuid)
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID) {
    checkFeatureGuid(featureGuid, formInstanceGuid)
    formInstanceQuestionStore.delete(formInstanceGuid, questionGuid)
  }

  private fun checkFeatureGuid(featureGuid: UUID, formInstanceGuid: UUID) {
    if (formInstanceStore.get(formInstanceGuid)?.featureGuid != featureGuid) throw FormInstanceNotFound()
  }
}
