package io.limberapp.web.api.formInstance.api.question

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

internal class FormInstanceQuestionApi(private val fetch: Fetch, private val json: Json) {

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
