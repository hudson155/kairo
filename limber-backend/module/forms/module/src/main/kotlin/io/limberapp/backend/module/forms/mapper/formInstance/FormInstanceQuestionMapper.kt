package io.limberapp.backend.module.forms.mapper.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionModel
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceRadioSelectorQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceYesNoQuestionRep
import java.time.Clock
import java.time.ZonedDateTime
import java.util.*
import kotlin.reflect.KClass

internal class FormInstanceQuestionMapper @Inject constructor(
    private val clock: Clock,
) {
  fun model(formInstanceGuid: UUID, questionGuid: UUID, rep: FormInstanceQuestionRep.Creation) = when (rep) {
    is FormInstanceDateQuestionRep.Creation -> FormInstanceDateQuestionModel(
        createdDate = ZonedDateTime.now(clock),
        formInstanceGuid = formInstanceGuid,
        questionGuid = questionGuid,
        date = rep.date,
    )
    is FormInstanceRadioSelectorQuestionRep.Creation -> FormInstanceRadioSelectorQuestionModel(
        createdDate = ZonedDateTime.now(clock),
        formInstanceGuid = formInstanceGuid,
        questionGuid = questionGuid,
        selections = listOf(rep.selection),
    )
    is FormInstanceTextQuestionRep.Creation -> FormInstanceTextQuestionModel(
        createdDate = ZonedDateTime.now(clock),
        formInstanceGuid = formInstanceGuid,
        questionGuid = questionGuid,
        text = rep.text,
    )
    is FormInstanceYesNoQuestionRep.Creation -> FormInstanceYesNoQuestionModel(
        createdDate = ZonedDateTime.now(clock),
        formInstanceGuid = formInstanceGuid,
        questionGuid = questionGuid,
        yes = rep.yes,
    )
    else -> unknownFormInstanceQuestion(rep::class)
  }

  fun completeRep(model: FormInstanceQuestionModel) = when (model) {
    is FormInstanceDateQuestionModel -> FormInstanceDateQuestionRep.Complete(
        createdDate = model.createdDate,
        questionGuid = model.questionGuid,
        date = model.date,
    )
    is FormInstanceRadioSelectorQuestionModel -> FormInstanceRadioSelectorQuestionRep.Complete(
        createdDate = model.createdDate,
        questionGuid = model.questionGuid,
        selection = model.selections.single(),
    )
    is FormInstanceTextQuestionModel -> FormInstanceTextQuestionRep.Complete(
        createdDate = model.createdDate,
        questionGuid = model.questionGuid,
        text = model.text,
    )
    is FormInstanceYesNoQuestionModel -> FormInstanceYesNoQuestionRep.Complete(
        createdDate = model.createdDate,
        questionGuid = model.questionGuid,
        yes = model.yes,
    )
    else -> unknownFormInstanceQuestion(model::class)
  }

  private fun unknownFormInstanceQuestion(kClass: KClass<*>): Nothing {
    unknownType("form instance question", kClass)
  }
}
