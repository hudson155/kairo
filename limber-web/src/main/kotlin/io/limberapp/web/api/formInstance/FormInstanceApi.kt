package io.limberapp.web.api.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.formInstance.api.question.FormInstanceQuestionApi

internal class FormInstanceApi(private val fetch: Fetch) {

    suspend fun post(rep: FormInstanceRep.Creation): FormInstanceRep.Complete {
        return fetch.post("/form-instances", rep)
            .unsafeCast<FormInstanceRep.Complete>()
    }

    suspend fun getByFeatureId(featureId: String): List<FormInstanceRep.Complete> {
        return fetch.get("/form-instances", listOf("featureId" to featureId))
            .unsafeCast<List<FormInstanceRep.Complete>>()
    }

    suspend fun delete(formInstanceId: String) {
        fetch.delete("/form-instances/$formInstanceId")
    }

    val questions = FormInstanceQuestionApi(fetch)
}
