package io.limberapp.backend.module.forms.mapper.api.formTemplate

import com.google.inject.Inject
import com.piperframework.util.uuid.unknown
import com.piperframework.util.uuid.uuidGenerator.UuidGenerator
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateDateQuestionRep
import io.limberapp.backend.module.forms.rep.formTemplate.formTemplateQuestion.FormTemplateTextQuestionRep
import io.limberapp.backend.module.orgs.model.formTemplate.FormTemplateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateDateQuestionModel
import io.limberapp.backend.module.orgs.model.formTemplate.formTemplateQuestion.FormTemplateTextQuestionModel

internal class FormTemplateQuestionMapper @Inject constructor(
    private val uuidGenerator: UuidGenerator
) {

    fun defaultModels() = listOf(
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = "Additional Information",
            helpText = null,
            width = FormTemplateQuestionModel.Width.FULL_WIDTH,
            multiLine = true,
            placeholder = null,
            validator = null
        ),
        FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = "Worker Name",
            helpText = null,
            width = FormTemplateQuestionModel.Width.HALF_WIDTH,
            multiLine = false,
            placeholder = null,
            validator = null
        ),
        FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            label = "Date",
            helpText = null,
            width = FormTemplateQuestionModel.Width.HALF_WIDTH,
            earliest = null,
            latest = null
        )
    )

    fun model(rep: FormTemplateQuestionRep.Creation) = when (rep) {
        is FormTemplateDateQuestionRep.Creation -> FormTemplateDateQuestionModel(
            id = uuidGenerator.generate(),
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Creation -> FormTemplateTextQuestionModel(
            id = uuidGenerator.generate(),
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        else -> unknown("form template question", rep::class)
    }

    fun completeRep(model: FormTemplateQuestionModel) = when (model) {
        is FormTemplateDateQuestionModel -> FormTemplateDateQuestionRep.Complete(
            id = model.id,
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            earliest = model.earliest,
            latest = model.latest
        )
        is FormTemplateTextQuestionModel -> FormTemplateTextQuestionRep.Complete(
            id = uuidGenerator.generate(),
            label = model.label,
            helpText = model.helpText,
            width = model.width,
            maxLength = if (model.multiLine) 10_000 else 200,
            multiLine = model.multiLine,
            placeholder = model.placeholder,
            validator = model.validator
        )
        else -> unknown("form template question", model::class)
    }

    fun update(rep: FormTemplateQuestionRep.Update) = when (rep) {
        is FormTemplateDateQuestionRep.Update -> FormTemplateDateQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            earliest = rep.earliest,
            latest = rep.latest
        )
        is FormTemplateTextQuestionRep.Update -> FormTemplateTextQuestionModel.Update(
            label = rep.label,
            helpText = rep.helpText,
            width = rep.width,
            multiLine = rep.multiLine,
            placeholder = rep.placeholder,
            validator = rep.validator
        )
        else -> unknown("form template question", rep::class)
    }
}
