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
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
) : FormInstanceQuestionService {
  override fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    val questionGuid = requireNotNull(model.questionGuid)
    formInstanceStore.get(featureGuid = featureGuid, formInstanceGuid = model.formInstanceGuid)
      .singleNullOrThrow()
      .ifNull { throw FormInstanceNotFound() }
    val existingFormInstanceQuestionModel = formInstanceQuestionStore.get(
      formInstanceGuid = model.formInstanceGuid,
      questionGuid = questionGuid
    ).singleNullOrThrow()
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
    formInstanceStore.get(featureGuid = featureGuid, formInstanceGuid = formInstanceGuid)
      .singleNullOrThrow()
      .ifNull { throw FormInstanceNotFound() }
    return formInstanceQuestionStore.get(formInstanceGuid = formInstanceGuid)
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID) {
    formInstanceStore.get(featureGuid = featureGuid, formInstanceGuid = formInstanceGuid)
      .singleNullOrThrow()
      .ifNull { throw FormInstanceNotFound() }
    formInstanceQuestionStore.delete(formInstanceGuid, questionGuid)
  }
}
