package io.limberapp.web.api.formInstance.api.question

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.web.api.Fetch

internal class FormInstanceQuestionApi(private val fetch: Fetch) {

    suspend fun put(
        formInstanceId: String,
        rep: FormInstanceQuestionRep.Creation
    ): FormInstanceQuestionRep.Complete {
        return fetch.put("/form-instances/$formInstanceId/questions", rep)
            .unsafeCast<FormInstanceQuestionRep.Complete>()
    }

    suspend fun delete(formInstanceId: String, questionId: String) {
        fetch.delete("/form-instances/$formInstanceId/questions/$questionId")
    }
}
