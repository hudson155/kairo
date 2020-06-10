package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateNotFound
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FormTemplateQuestionServiceImpl @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val formTemplateStore: FormTemplateStore,
  private val formTemplateQuestionStore: FormTemplateQuestionStore
) : FormTemplateQuestionService {
  override fun createDefaults(featureGuid: UUID, formTemplateGuid: UUID): List<FormTemplateQuestionModel> {
    if (!formTemplateStore.existsAndHasFeatureGuid(formTemplateGuid, featureGuid = featureGuid)) {
      throw FormTemplateNotFound()
    }
    require(formTemplateQuestionStore.getByFormTemplateGuid(formTemplateGuid).isEmpty())
    val questions = listOf(
      FormTemplateTextQuestionModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        formTemplateGuid = formTemplateGuid,
        label = "Worker name",
        helpText = null,
        multiLine = false,
        placeholder = null,
        validator = null
      ),
      FormTemplateDateQuestionModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        formTemplateGuid = formTemplateGuid,
        label = "Date",
        helpText = null,
        earliest = null,
        latest = null
      ),
      FormTemplateTextQuestionModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        formTemplateGuid = formTemplateGuid,
        label = "Description",
        helpText = null,
        multiLine = true,
        placeholder = null,
        validator = null
      ),
      FormTemplateRadioSelectorQuestionModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        formTemplateGuid = formTemplateGuid,
        label = "Two options",
        helpText = null,
        options = listOf("test_option_one", "test_option_two")
      )
    )
    formTemplateQuestionStore.create(questions)
    return questions
  }

  override fun create(featureGuid: UUID, model: FormTemplateQuestionModel, rank: Int?): FormTemplateQuestionModel {
    if (!formTemplateStore.existsAndHasFeatureGuid(model.formTemplateGuid, featureGuid = featureGuid)) {
      throw FormTemplateNotFound()
    }
    return formTemplateQuestionStore.create(model, rank)
  }

  override fun getByFormTemplateGuid(featureGuid: UUID, formTemplateGuid: UUID): List<FormTemplateQuestionModel> {
    if (!formTemplateStore.existsAndHasFeatureGuid(formTemplateGuid, featureGuid = featureGuid)) {
      throw FormTemplateNotFound()
    }
    return formTemplateQuestionStore.getByFormTemplateGuid(formTemplateGuid)
  }

  override fun update(
    featureGuid: UUID,
    formTemplateGuid: UUID,
    questionGuid: UUID,
    update: FormTemplateQuestionModel.Update
  ): FormTemplateQuestionModel {
    if (!formTemplateStore.existsAndHasFeatureGuid(formTemplateGuid, featureGuid = featureGuid)) {
      throw FormTemplateNotFound()
    }
    if (!formTemplateQuestionStore.existsAndHasFormTemplateGuid(questionGuid, formTemplateGuid = formTemplateGuid)) {
      throw FormTemplateQuestionNotFound()
    }
    return formTemplateQuestionStore.update(questionGuid, update)
  }

  override fun delete(featureGuid: UUID, formTemplateGuid: UUID, questionGuid: UUID) {
    if (!formTemplateStore.existsAndHasFeatureGuid(formTemplateGuid, featureGuid = featureGuid)) {
      throw FormTemplateNotFound()
    }
    if (!formTemplateQuestionStore.existsAndHasFormTemplateGuid(questionGuid, formTemplateGuid = formTemplateGuid)) {
      throw FormTemplateQuestionNotFound()
    }
    formTemplateQuestionStore.delete(questionGuid)
  }
}
