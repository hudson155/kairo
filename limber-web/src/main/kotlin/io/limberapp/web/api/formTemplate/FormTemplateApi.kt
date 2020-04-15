package io.limberapp.web.api.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.Fetch
import io.limberapp.web.api.formTemplate.api.question.FormTemplateQuestionApi

internal object FormTemplateApi {

    suspend fun post(rep: FormTemplateRep.Creation): FormTemplateRep.Complete {
        return Fetch.post("/form-templates", rep)
            .unsafeCast<FormTemplateRep.Complete>()
    }

    suspend fun getByFeatureId(featureId: String): List<FormTemplateRep.Complete> {
        return Fetch.get("/form-templates", listOf("featureId" to featureId))
            .unsafeCast<List<FormTemplateRep.Complete>>()
    }

    suspend fun patch(formTemplateId: String, rep: FormTemplateRep.Update): FormTemplateRep.Complete {
        return Fetch.patch("/form-templates/$formTemplateId", rep)
            .unsafeCast<FormTemplateRep.Complete>()
    }

    suspend fun delete(formTemplateId: String) {
        Fetch.delete("/form-templates/$formTemplateId")
    }

    val Questions = FormTemplateQuestionApi
}
