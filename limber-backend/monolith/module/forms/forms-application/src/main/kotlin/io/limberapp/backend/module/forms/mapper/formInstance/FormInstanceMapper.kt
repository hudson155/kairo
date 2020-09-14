package io.limberapp.backend.module.forms.mapper.formInstance

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

internal class FormInstanceMapper @Inject constructor(
  private val clock: Clock,
  private val uuidGenerator: UuidGenerator,
  private val formInstanceQuestionMapper: FormInstanceQuestionMapper,
) {
  fun model(featureGuid: UUID, rep: FormInstanceRep.Creation) = FormInstanceModel(
    guid = uuidGenerator.generate(),
    createdDate = LocalDateTime.now(clock),
    featureGuid = featureGuid,
    formTemplateGuid = rep.formTemplateGuid,
    number = 0,
    submittedDate = null,
    creatorAccountGuid = rep.creatorAccountGuid
  )

  fun summaryRep(model: FormInstanceModel) = FormInstanceRep.Summary(
    guid = model.guid,
    createdDate = model.createdDate,
    formTemplateGuid = model.formTemplateGuid,
    number = model.number,
    submittedDate = model.submittedDate,
    creatorAccountGuid = model.creatorAccountGuid
  )

  fun completeRep(model: FormInstanceModel, questions: Set<FormInstanceQuestionModel>) = FormInstanceRep.Complete(
    guid = model.guid,
    createdDate = model.createdDate,
    formTemplateGuid = model.formTemplateGuid,
    number = model.number,
    submittedDate = model.submittedDate,
    creatorAccountGuid = model.creatorAccountGuid,
    questions = questions.map { formInstanceQuestionMapper.completeRep(it) }.toSet()
  )

  fun update(rep: FormInstanceRep.Update) = FormInstanceModel.Update(
    submittedDate = if (rep.submitted == true) LocalDateTime.now(clock) else null
  )
}
