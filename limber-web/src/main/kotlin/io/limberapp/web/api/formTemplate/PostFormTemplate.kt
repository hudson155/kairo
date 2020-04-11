package io.limberapp.web.api.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.Api

internal suspend fun postFormTemplate(rep: FormTemplateRep.Creation): FormTemplateRep.Complete {
    return Api.post("/form-templates", rep)
        .unsafeCast<FormTemplateRep.Complete>()
}
