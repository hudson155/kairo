package io.limberapp.web.api.formTemplate.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.api.Api

internal suspend fun putFormTemplateQuestion(
    formTemplateId: String,
    rep: FormTemplateQuestionRep.Creation
): FormTemplateQuestionRep.Complete {
    return Api.put("/form-templates/$formTemplateId/questions", rep)
        .unsafeCast<FormTemplateQuestionRep.Complete>()
}
