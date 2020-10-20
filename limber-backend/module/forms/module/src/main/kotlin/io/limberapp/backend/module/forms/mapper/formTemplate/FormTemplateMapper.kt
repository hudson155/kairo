package io.limberapp.backend.module.forms.mapper.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateModel
import io.limberapp.backend.module.forms.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.common.util.uuid.UuidGenerator
import java.time.Clock
import java.time.ZonedDateTime
import java.util.*

internal class FormTemplateMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val formTemplateQuestionMapper: FormTemplateQuestionMapper,
) {
  fun model(featureGuid: UUID, rep: FormTemplateRep.Creation) = FormTemplateModel(
      guid = uuidGenerator.generate(),
      createdDate = ZonedDateTime.now(clock),
      featureGuid = featureGuid,
      title = rep.title,
      description = rep.description
  )

  fun summaryRep(model: FormTemplateModel) = FormTemplateRep.Summary(
      guid = model.guid,
      createdDate = model.createdDate,
      title = model.title,
      description = model.description
  )

  fun completeRep(model: FormTemplateModel, questions: List<FormTemplateQuestionModel>) = FormTemplateRep.Complete(
      guid = model.guid,
      createdDate = model.createdDate,
      title = model.title,
      description = model.description,
      questions = questions.map { formTemplateQuestionMapper.completeRep(it) }
  )

  fun update(rep: FormTemplateRep.Update) = FormTemplateModel.Update(
      title = rep.title,
      description = rep.description
  )
}
