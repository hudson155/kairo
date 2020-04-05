package io.limberapp.backend.module.forms.mapper.formInstance

import com.google.inject.Inject
import com.piperframework.util.unknownType
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID
import kotlin.reflect.KClass

internal class FormInstanceQuestionMapper @Inject constructor(
    private val clock: Clock
) {

    fun model(formTemplateQuestionId: UUID, rep: FormInstanceQuestionRep.Creation) = when (rep) {
        is FormInstanceDateQuestionRep.Creation -> FormInstanceDateQuestionModel(
            created = LocalDateTime.now(clock),
            formTemplateQuestionId = formTemplateQuestionId,
            date = rep.date
        )
        is FormInstanceTextQuestionRep.Creation -> FormInstanceTextQuestionModel(
            created = LocalDateTime.now(clock),
            formTemplateQuestionId = formTemplateQuestionId,
            text = rep.text
        )
        is FormInstanceRadioSelectorQuestionRep.Creation -> FormInstanceRadioSelectorQuestionModel(
            created = LocalDateTime.now(clock),
            formTemplateQuestionId = rep.formTemplateQuestionId,
            selection = rep.selection
        )
        else -> unknownFormInstanceQuestion(rep::class)
    }

    fun completeRep(model: FormInstanceQuestionModel) = when (model) {
        is FormInstanceDateQuestionModel -> FormInstanceDateQuestionRep.Complete(
            created = model.created,
            formTemplateQuestionId = model.formTemplateQuestionId,
            date = model.date
        )
        is FormInstanceTextQuestionModel -> FormInstanceTextQuestionRep.Complete(
            created = model.created,
            formTemplateQuestionId = model.formTemplateQuestionId,
            text = model.text
        )
        is FormInstanceRadioSelectorQuestionModel -> FormInstanceRadioSelectorQuestionRep.Complete(
            created = model.created,
            formTemplateQuestionId = model.formTemplateQuestionId,
            selection = model.selection
        )
        else -> unknownFormInstanceQuestion(model::class)
    }

    fun update(model: FormInstanceQuestionModel) = when (model) {
        is FormInstanceDateQuestionModel -> FormInstanceDateQuestionModel.Update(date = model.date)
        is FormInstanceTextQuestionModel -> FormInstanceTextQuestionModel.Update(text = model.text)
        else -> unknownFormInstanceQuestion(model::class)
    }

    private fun unknownFormInstanceQuestion(kClass: KClass<*>): Nothing {
        unknownType("form instance question", kClass)
    }
}
