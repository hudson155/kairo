package io.limberapp.web.api.formInstance.api.question

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.web.api.Fetch

internal class FormInstanceQuestionApi {

    suspend fun put(
        formInstanceId: String,
        rep: FormInstanceQuestionRep.Creation
    ): FormInstanceQuestionRep.Complete {
        return Fetch.put("/form-instances/$formInstanceId/questions", rep)
            .unsafeCast<FormInstanceQuestionRep.Complete>()
    }

    suspend fun delete(formInstanceId: String, questionId: String) {
        Fetch.delete("/form-instances/$formInstanceId/questions/$questionId")
    }
}
