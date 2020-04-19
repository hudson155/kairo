package io.limberapp.web.api.formInstance.api.question

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.json

internal class FormInstanceQuestionApi(private val fetch: Fetch) {

    suspend fun put(
        formInstanceId: String,
        rep: FormInstanceQuestionRep.Creation
    ): FormInstanceQuestionRep.Complete {
        val string = fetch.put("/form-instances/$formInstanceId/questions", rep)
        return json.parse(string)
    }

    suspend fun delete(formInstanceId: String, questionId: String) {
        fetch.delete("/form-instances/$formInstanceId/questions/$questionId")
    }
}
