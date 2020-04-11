package io.limberapp.web.api.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.Api

internal suspend fun patchFormTemplate(formTemplateId: String, rep: FormTemplateRep.Update): FormTemplateRep.Complete {
    return Api.patch("/form-templates/$formTemplateId", rep)
        .unsafeCast<FormTemplateRep.Complete>()
}
