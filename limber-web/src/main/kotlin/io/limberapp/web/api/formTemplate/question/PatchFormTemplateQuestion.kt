package io.limberapp.web.api.formTemplate.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.api.Api

internal suspend fun patchFormTemplateQuestion(
    formTemplateId: String,
    questionId: String,
    rep: FormTemplateQuestionRep.Update
): FormTemplateQuestionRep.Complete {
    return Api.patch("/form-templates/$formTemplateId/questions/$questionId", rep)
        .unsafeCast<FormTemplateQuestionRep.Complete>()
}
