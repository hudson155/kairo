package io.limberapp.backend.module.forms.mapper.formInstance

import com.google.inject.Inject
import com.piperframework.util.unknown
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceDateQuestionModel
import io.limberapp.backend.module.forms.model.formInstance.formInstanceQuestion.FormInstanceTextQuestionModel
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceDateQuestionRep
import io.limberapp.backend.module.forms.rep.formInstance.formInstanceQuestion.FormInstanceTextQuestionRep
import java.time.Clock
import java.time.LocalDateTime
import kotlin.reflect.KClass

internal class FormInstanceQuestionMapper @Inject constructor(
    private val clock: Clock
) {

    fun model(rep: FormInstanceQuestionRep.Creation) = when (rep) {
        is FormInstanceDateQuestionRep.Creation -> FormInstanceDateQuestionModel(
            created = LocalDateTime.now(clock),
            formTemplateQuestionId = rep.formTemplateQuestionId,
            date = rep.date
        )
        is FormInstanceTextQuestionRep.Creation -> FormInstanceTextQuestionModel(
            created = LocalDateTime.now(clock),
            formTemplateQuestionId = rep.formTemplateQuestionId,
            text = rep.text
        )
        else -> unknown(rep::class)
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
        else -> unknown(model::class)
    }

    private fun unknown(klass: KClass<*>): Nothing =
        unknown("form instance question", klass)
}
