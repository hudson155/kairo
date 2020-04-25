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
    fun model(questionGuid: UUID, rep: FormInstanceQuestionRep.Creation) = when (rep) {
        is FormInstanceDateQuestionRep.Creation -> FormInstanceDateQuestionModel(
            createdDate = LocalDateTime.now(clock),
            questionGuid = questionGuid,
            date = rep.date
        )
        is FormInstanceRadioSelectorQuestionRep.Creation -> FormInstanceRadioSelectorQuestionModel(
            createdDate = LocalDateTime.now(clock),
            questionGuid = questionGuid,
            selection = rep.selection
        )
        is FormInstanceTextQuestionRep.Creation -> FormInstanceTextQuestionModel(
            createdDate = LocalDateTime.now(clock),
            questionGuid = questionGuid,
            text = rep.text
        )
        else -> unknownFormInstanceQuestion(rep::class)
    }

    fun completeRep(model: FormInstanceQuestionModel) = when (model) {
        is FormInstanceDateQuestionModel -> FormInstanceDateQuestionRep.Complete(
            createdDate = model.createdDate,
            questionGuid = model.questionGuid,
            date = model.date
        )
        is FormInstanceRadioSelectorQuestionModel -> FormInstanceRadioSelectorQuestionRep.Complete(
            createdDate = model.createdDate,
            questionGuid = model.questionGuid,
            selection = model.selection
        )
        is FormInstanceTextQuestionModel -> FormInstanceTextQuestionRep.Complete(
            createdDate = model.createdDate,
            questionGuid = model.questionGuid,
            text = model.text
        )
        else -> unknownFormInstanceQuestion(model::class)
    }

    fun update(model: FormInstanceQuestionModel) = when (model) {
        is FormInstanceDateQuestionModel -> FormInstanceDateQuestionModel.Update(date = model.date)
        is FormInstanceRadioSelectorQuestionModel ->
            FormInstanceRadioSelectorQuestionModel.Update(selection = model.selection)
        is FormInstanceTextQuestionModel -> FormInstanceTextQuestionModel.Update(text = model.text)
        else -> unknownFormInstanceQuestion(model::class)
    }

    private fun unknownFormInstanceQuestion(kClass: KClass<*>): Nothing {
        unknownType("form instance question", kClass)
    }
}
