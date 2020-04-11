package io.limberapp.web.api.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.api.Api

internal suspend fun postFormInstance(rep: FormInstanceRep.Creation): FormInstanceRep.Complete {
    return Api.post("/form-instances", rep)
        .unsafeCast<FormInstanceRep.Complete>()
}
