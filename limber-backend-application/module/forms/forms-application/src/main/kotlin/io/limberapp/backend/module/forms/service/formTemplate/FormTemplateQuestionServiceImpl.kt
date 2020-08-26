package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.finder.Finder
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionFinder
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FormTemplateQuestionServiceImpl @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val formTemplateQuestionStore: FormTemplateQuestionStore,
) : FormTemplateQuestionService,
  Finder<FormTemplateQuestionModel, FormTemplateQuestionFinder> by formTemplateQuestionStore {
  override fun createDefaults(featureGuid: UUID, formTemplateGuid: UUID) =
    defaultQuestions(formTemplateGuid).map { formTemplateQuestionStore.create(featureGuid, it) }

  private fun defaultQuestions(formTemplateGuid: UUID) = listOf(
    FormTemplateTextQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = "Worker name",
      helpText = null,
      required = true,
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
      required = true,
      earliest = null,
      latest = null
    ),
    FormTemplateTextQuestionModel(
      guid = uuidGenerator.generate(),
      createdDate = LocalDateTime.now(clock),
      formTemplateGuid = formTemplateGuid,
      label = "Description",
      helpText = null,
      required = false,
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
      required = false,
      options = listOf("test_option_one", "test_option_two")
    )
  )

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
