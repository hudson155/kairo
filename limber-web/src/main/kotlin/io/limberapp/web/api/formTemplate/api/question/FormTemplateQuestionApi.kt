package io.limberapp.web.api.formTemplate.api.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.api.Fetch

internal class FormTemplateQuestionApi(private val fetch: Fetch) {

    suspend fun put(
        formTemplateId: String,
        rep: FormTemplateQuestionRep.Creation
    ): FormTemplateQuestionRep.Complete {
        return fetch.put("/form-templates/$formTemplateId/questions", rep)
            .unsafeCast<FormTemplateQuestionRep.Complete>()
    }

    suspend fun patch(
        formTemplateId: String,
        questionId: String,
        rep: FormTemplateQuestionRep.Update
    ): FormTemplateQuestionRep.Complete {
        return fetch.patch("/form-templates/$formTemplateId/questions/$questionId", rep)
            .unsafeCast<FormTemplateQuestionRep.Complete>()
    }

    suspend fun delete(formTemplateId: String, questionId: String) {
        fetch.delete("/form-templates/$formTemplateId/questions/$questionId")
    }
}
