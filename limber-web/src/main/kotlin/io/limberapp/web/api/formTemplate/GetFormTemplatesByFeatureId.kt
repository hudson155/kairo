package io.limberapp.web.api.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.api.Api

internal suspend fun getFormTemplatesByFeatureId(featureId: String): List<FormTemplateRep.Complete> {
    return Api.get("/form-templates", listOf("featureId" to featureId))
        .unsafeCast<List<FormTemplateRep.Complete>>()
}
