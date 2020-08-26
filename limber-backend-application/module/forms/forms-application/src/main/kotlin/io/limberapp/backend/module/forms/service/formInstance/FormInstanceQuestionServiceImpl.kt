package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import com.piperframework.finder.Finder
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceQuestionMapper
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionFinder
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore
import java.util.*

internal class FormInstanceQuestionServiceImpl @Inject constructor(
  private val formInstanceQuestionStore: FormInstanceQuestionStore,
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
) : FormInstanceQuestionService,
  Finder<FormInstanceQuestionModel, FormInstanceQuestionFinder> by formInstanceQuestionStore {
  override fun upsert(featureGuid: UUID, model: FormInstanceQuestionModel): FormInstanceQuestionModel {
    val questionGuid = requireNotNull(model.questionGuid)
    val existingFormInstanceQuestionModel = formInstanceQuestionStore.findOnlyOrNull {
      formInstanceGuid(model.formInstanceGuid)
      questionGuid(questionGuid)
    }
    return if (existingFormInstanceQuestionModel == null) {
      formInstanceQuestionStore.create(featureGuid, model)
    } else {
      formInstanceQuestionStore.update(
        featureGuid = featureGuid,
        formInstanceGuid = model.formInstanceGuid,
        questionGuid = questionGuid,
        update = formInstanceQuestionMapper.update(model)
      )
    }
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID, questionGuid: UUID) =
    formInstanceQuestionStore.delete(featureGuid, formInstanceGuid, questionGuid)
}
