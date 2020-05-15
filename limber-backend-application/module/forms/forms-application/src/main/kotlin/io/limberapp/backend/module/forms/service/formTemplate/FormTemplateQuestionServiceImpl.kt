package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.exception.formTemplate.FormTemplateQuestionNotFound
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FormTemplateQuestionServiceImpl @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val formTemplateQuestionStore: FormTemplateQuestionStore
) : FormTemplateQuestionService {
  override fun createDefaults(formTemplateGuid: UUID): List<FormTemplateQuestionModel> {
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

  override fun create(model: FormTemplateQuestionModel, rank: Int?) =
    formTemplateQuestionStore.create(model, rank)

  override fun getByFormTemplateGuid(formTemplateGuid: UUID) =
    formTemplateQuestionStore.getByFormTemplateGuid(formTemplateGuid)

  override fun update(
    formTemplateGuid: UUID,
    questionGuid: UUID,
    update: FormTemplateQuestionModel.Update
  ): FormTemplateQuestionModel {
    checkFormTemplateGuid(formTemplateGuid, questionGuid)
    return formTemplateQuestionStore.update(questionGuid, update)
  }

  override fun delete(formTemplateGuid: UUID, questionGuid: UUID) {
    checkFormTemplateGuid(formTemplateGuid, questionGuid)
    formTemplateQuestionStore.delete(questionGuid)
  }

  private fun checkFormTemplateGuid(formTemplateGuid: UUID, questionGuid: UUID) {
    if (formTemplateQuestionStore.get(questionGuid)?.formTemplateGuid != formTemplateGuid) {
      throw FormTemplateQuestionNotFound()
    }
  }
}
