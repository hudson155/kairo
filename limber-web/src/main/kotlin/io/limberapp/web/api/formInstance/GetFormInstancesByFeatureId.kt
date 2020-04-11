package io.limberapp.web.api.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.api.Api

internal suspend fun getFormInstancesByFeatureId(featureId: String): List<FormInstanceRep.Complete> {
    return Api.get("/form-instances", listOf("featureId" to featureId))
        .unsafeCast<List<FormInstanceRep.Complete>>()
}
