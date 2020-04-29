package io.limberapp.backend.module.forms.mapper.formInstance

import com.google.inject.Inject
import com.piperframework.util.uuid.UuidGenerator
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import java.time.Clock
import java.time.LocalDateTime

internal class FormInstanceMapper @Inject constructor(
    private val clock: Clock,
    private val uuidGenerator: UuidGenerator,
    private val formInstanceQuestionMapper: FormInstanceQuestionMapper
) {
    fun model(rep: FormInstanceRep.Creation) = FormInstanceModel(
        guid = uuidGenerator.generate(),
        createdDate = LocalDateTime.now(clock),
        featureGuid = rep.featureGuid,
        formTemplateGuid = rep.formTemplateGuid
    )

    fun summaryRep(model: FormInstanceModel) = FormInstanceRep.Summary(
        guid = model.guid,
        createdDate = model.createdDate,
        featureGuid = model.featureGuid,
        formTemplateGuid = model.formTemplateGuid
    )

    fun completeRep(model: FormInstanceModel, questions: List<FormInstanceQuestionModel>) = FormInstanceRep.Complete(
        guid = model.guid,
        createdDate = model.createdDate,
        featureGuid = model.featureGuid,
        formTemplateGuid = model.formTemplateGuid,
        questions = questions.map { formInstanceQuestionMapper.completeRep(it) }
    )
}
