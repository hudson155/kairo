package io.limberapp.web.api.formInstance

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.api.formInstance.api.question.FormInstanceQuestionApi

internal class FormInstanceApi(private val fetch: Fetch, private val json: Json) {

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

    val questions = FormInstanceQuestionApi(fetch, json)
}
