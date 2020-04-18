package io.limberapp.web.api.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.formInstance.api.question.FormInstanceQuestionApi
import io.limberapp.web.api.json

internal class FormInstanceApi(private val fetch: Fetch) {

    suspend fun post(rep: FormInstanceRep.Creation): FormInstanceRep.Complete {
        val string = fetch.post("/form-instances", rep)
        return json.parse(string)
    }

    suspend fun getByFeatureId(featureId: String): List<FormInstanceRep.Complete> {
        val string = fetch.get("/form-instances", listOf("featureId" to featureId))
        return json.parse(string)
    }

    suspend fun delete(formInstanceId: String) {
        fetch.delete("/form-instances/$formInstanceId")
    }

    val questions = FormInstanceQuestionApi(fetch)
}
