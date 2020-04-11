package io.limberapp.web.api.formInstance.question

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.web.api.Api

internal suspend fun putFormInstanceQuestion(
    formInstanceId: String,
    rep: FormInstanceQuestionRep.Creation
): FormInstanceQuestionRep.Complete {
    return Api.put("/form-instances/$formInstanceId/questions", rep)
        .unsafeCast<FormInstanceQuestionRep.Complete>()
}
