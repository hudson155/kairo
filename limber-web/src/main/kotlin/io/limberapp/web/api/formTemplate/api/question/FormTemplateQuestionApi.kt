package io.limberapp.web.api.formTemplate.api.question

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

internal class FormTemplateQuestionApi(private val fetch: Fetch, private val json: Json) {

    suspend fun put(
        formTemplateId: String,
        rep: FormTemplateQuestionRep.Creation
    ): FormTemplateQuestionRep.Complete {
        val string = fetch.put("/form-templates/$formTemplateId/questions", rep)
        return json.parse(string)
    }

    suspend fun patch(
        formTemplateId: String,
        questionId: String,
        rep: FormTemplateQuestionRep.Update
    ): FormTemplateQuestionRep.Complete {
        val string = fetch.patch("/form-templates/$formTemplateId/questions/$questionId", rep)
        return json.parse(string)
    }

    suspend fun delete(formTemplateId: String, questionId: String) {
        fetch.delete("/form-templates/$formTemplateId/questions/$questionId")
    }
}
