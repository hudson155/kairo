package io.limberapp.web.api.formTemplate

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.formTemplate.api.question.FormTemplateQuestionApi

internal class FormTemplateApi(private val fetch: Fetch, private val json: Json) {

    suspend fun post(rep: FormTemplateRep.Creation): FormTemplateRep.Complete {
        val string = fetch.post("/form-templates", rep)
        return json.parse(string)
    }

    suspend fun getByFeatureId(featureId: String): List<FormTemplateRep.Complete> {
        val string = fetch.get("/form-templates", listOf("featureId" to featureId))
        return json.parse(string)
    }

    suspend fun patch(formTemplateId: String, rep: FormTemplateRep.Update): FormTemplateRep.Complete {
        val string = fetch.patch("/form-templates/$formTemplateId", rep)
        return json.parse(string)
    }

    suspend fun delete(formTemplateId: String) {
        fetch.delete("/form-templates/$formTemplateId")
    }

    val questions = FormTemplateQuestionApi(fetch, json)
}
