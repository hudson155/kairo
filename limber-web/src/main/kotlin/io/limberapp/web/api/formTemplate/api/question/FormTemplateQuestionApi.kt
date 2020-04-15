package io.limberapp.web.api.formTemplate.api.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.web.api.Fetch

internal object FormTemplateQuestionApi {

    suspend fun put(
        formTemplateId: String,
        rep: FormTemplateQuestionRep.Creation
    ): FormTemplateQuestionRep.Complete {
        return Fetch.put("/form-templates/$formTemplateId/questions", rep)
            .unsafeCast<FormTemplateQuestionRep.Complete>()
    }

    suspend fun patch(
        formTemplateId: String,
        questionId: String,
        rep: FormTemplateQuestionRep.Update
    ): FormTemplateQuestionRep.Complete {
        return Fetch.patch("/form-templates/$formTemplateId/questions/$questionId", rep)
            .unsafeCast<FormTemplateQuestionRep.Complete>()
    }

    suspend fun delete(formTemplateId: String, questionId: String) {
        Fetch.delete("/form-templates/$formTemplateId/questions/$questionId")
    }
}
