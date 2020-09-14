package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.CannotReSubmitFormInstance
import io.limberapp.backend.module.forms.exception.formInstance.CannotSubmitFormBeforeAnsweringAllRequiredQuestions
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceFinder
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import io.limberapp.common.finder.Finder
import java.util.*

internal class FormInstanceServiceImpl @Inject constructor(
  private val formTemplateQuestionService: FormTemplateQuestionService,
  private val formInstanceQuestionService: FormInstanceQuestionService,
  private val formInstanceStore: FormInstanceStore,
) : FormInstanceService, Finder<FormInstanceModel, FormInstanceFinder> by formInstanceStore {
  override fun create(model: FormInstanceModel) =
    formInstanceStore.create(model)

  override fun update(featureGuid: UUID, formInstanceGuid: UUID, update: FormInstanceModel.Update): FormInstanceModel {
    if (update.submittedDate != null) validateBeforeSubmission(featureGuid, formInstanceGuid)
    return formInstanceStore.update(featureGuid, formInstanceGuid, update)
  }

  private fun validateBeforeSubmission(featureGuid: UUID, formInstanceGuid: UUID) {
    val formInstance = formInstanceStore.findOnlyOrNull { featureGuid(featureGuid); formInstanceGuid(formInstanceGuid) }
      ?: throw FormInstanceNotFound()
    if (formInstance.submittedDate != null) throw CannotReSubmitFormInstance()
    val formTemplateQuestions = formTemplateQuestionService.findAsSet {
      formTemplateGuid(formInstance.formTemplateGuid)
    }
    val formInstanceQuestions = formInstanceQuestionService.findAsSet {
      formInstanceGuid(formInstanceGuid)
    }.associateBy { it.questionGuid }
    val requiredQuestions = formTemplateQuestions.filter { it.required }
    val allRequiredQuestionsAreAnswered = requiredQuestions.all { it.guid in formInstanceQuestions }
    if (!allRequiredQuestionsAreAnswered) throw CannotSubmitFormBeforeAnsweringAllRequiredQuestions()
  }

  override fun delete(featureGuid: UUID, formInstanceGuid: UUID) =
    formInstanceStore.delete(featureGuid, formInstanceGuid)
}
