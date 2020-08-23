package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import java.util.*

internal class FormInstanceQuestionServiceImpl @Inject constructor(
  private val formInstanceStore: FormInstanceStore,
  private val formInstanceQuestionStore: FormInstanceQuestionStore,
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
) : FormInstanceQuestionService,
  Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> by formInstanceQuestionStore {
  override fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    val questionGuid = requireNotNull(model.questionGuid)
    formInstanceStore.findOnlyOrNull { featureGuid(featureGuid); formInstanceGuid(model.formInstanceGuid) }
      .ifNull { throw FormInstanceNotFound() }
    val existingFormInstanceQuestionModel = formInstanceQuestionStore.findOnlyOrNull {
      formInstanceGuid(model.formInstanceGuid)
      questionGuid(questionGuid)
    }
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

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID) {
    formInstanceStore.findOnlyOrNull { featureGuid(featureGuid); formInstanceGuid(formInstanceGuid) }
      .ifNull { throw FormInstanceNotFound() }
    formInstanceQuestionStore.delete(formInstanceGuid, questionGuid)
  }
}
